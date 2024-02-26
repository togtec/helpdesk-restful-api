package com.rodrigo.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.dtos.TecnicoRequestDTO;
import com.rodrigo.helpdesk.enums.Perfil;
import com.rodrigo.helpdesk.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;
import com.rodrigo.helpdesk.model.Pessoa;
import com.rodrigo.helpdesk.model.Tecnico;
import com.rodrigo.helpdesk.repositories.PessoaRepository;
import com.rodrigo.helpdesk.repositories.TecnicoRepository;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public Tecnico create(TecnicoRequestDTO objRequestDTO) {
        /*
         * id deve ser nulo - se houver um id na requisição, o método save fará um
         * update ao invés de salvar
         */
        objRequestDTO.setId(null);
        objRequestDTO.setSenha(encoder.encode(objRequestDTO.getSenha()));
        objRequestDTO.setDataCriacao(LocalDate.now());
        objRequestDTO.addPerfil(Perfil.CLIENTE);
        objRequestDTO.addPerfil(Perfil.TECNICO);
        validaCpfAndEmail(objRequestDTO);
        Tecnico newObj = new Tecnico(objRequestDTO);
        return tecnicoRepository.save(newObj);
    }

    public Tecnico findById(Long id) {
        Optional<Tecnico> obj = tecnicoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Tecnico> findAll() {
        return tecnicoRepository.findAll();
    }

    public void delete(Long id) {
        // se id não existe no banco, lança ObjectNotFoundException
        Tecnico obj = findById(id);

        // verifica se existem orderns de serviço em nome do técnico
        if (obj.getChamados().size() > 0)
            throw new DataIntegrityViolationException(
                    "Técnico possui ordens de serviço em seu nome e não pode ser excluído!");

        tecnicoRepository.deleteById(id);
    }

    public Tecnico update(Long id, TecnicoRequestDTO newObjDTO) {
        // define o id da url evitando falha de segurança caso haja outro id no objeto
        // que vem no corpo da requisiação
        newObjDTO.setId(id);
        newObjDTO.setSenha(encoder.encode(newObjDTO.getSenha()));
        // se id não existe no banco, lança ObjectNotFoundException
        Tecnico oldObj = findById(id);
        newObjDTO.setDataCriacao(oldObj.getDataCriacao());
        validaCpfAndEmail(newObjDTO);
        oldObj = new Tecnico(newObjDTO);
        return tecnicoRepository.save(oldObj);
    }

    private void validaCpfAndEmail(TecnicoRequestDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId())
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId())
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
    }

}