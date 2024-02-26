package com.rodrigo.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.dtos.ClienteRequestDTO;
import com.rodrigo.helpdesk.enums.Perfil;
import com.rodrigo.helpdesk.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.model.Pessoa;
import com.rodrigo.helpdesk.repositories.ClienteRepository;
import com.rodrigo.helpdesk.repositories.PessoaRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public Cliente create(ClienteRequestDTO objRequestDTO) {
        /*
         * id deve ser nulo - se houver um id na requisição, o método save fará um
         * update ao invés de salvar
         */
        objRequestDTO.setId(null);
        objRequestDTO.setSenha(encoder.encode(objRequestDTO.getSenha()));
        objRequestDTO.setDataCriacao(LocalDate.now());
        objRequestDTO.resetPerfis();
        objRequestDTO.addPerfil(Perfil.CLIENTE);
        validaCpfAndEmail(objRequestDTO);
        Cliente newObj = new Cliente(objRequestDTO);
        return clienteRepository.save(newObj);
    }

    public Cliente findById(Long id) {
        Optional<Cliente> obj = clienteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public void delete(Long id) {
        // se id não existe no banco, lança ObjectNotFoundException
        Cliente obj = findById(id);

        // verifica se existem orderns de serviço em nome do cliente
        if (obj.getChamados().size() > 0)
            throw new DataIntegrityViolationException(
                    "Cliente possui ordens de serviço em seu nome e não pode ser excluído!");

        clienteRepository.deleteById(id);
    }

    public Cliente update(Long id, ClienteRequestDTO newObjDTO) {
        // define o id da url evitando falha de segurança caso haja outro id no objeto
        // que vem no corpo da requisiação
        newObjDTO.setId(id);
        newObjDTO.setSenha(encoder.encode(newObjDTO.getSenha()));
        newObjDTO.resetPerfis();
        newObjDTO.addPerfil(Perfil.CLIENTE);
        // se id não existe no banco, lança ObjectNotFoundException
        Cliente oldObj = findById(id);
        newObjDTO.setDataCriacao(oldObj.getDataCriacao());
        validaCpfAndEmail(newObjDTO);
        oldObj = new Cliente(newObjDTO);
        return clienteRepository.save(oldObj);
    }

    private void validaCpfAndEmail(ClienteRequestDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId())
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId())
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
    }

}
