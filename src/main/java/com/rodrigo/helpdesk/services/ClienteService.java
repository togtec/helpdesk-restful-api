package com.rodrigo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.domain.Pessoa;
import com.rodrigo.helpdesk.domain.Cliente;
import com.rodrigo.helpdesk.domain.dtos.ClienteDTO;
import com.rodrigo.helpdesk.repositories.PessoaRepository;
import com.rodrigo.helpdesk.repositories.ClienteRepository;
import com.rodrigo.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired	
	private ClienteRepository clienteRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;	
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
		
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Cliente create(ClienteDTO objDTO) {
		/* vamos deixar o id nulo porque se houver um valor para id na requisição, 
		o método save fará um update ao invés de salvar */
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaCpfAndEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return clienteRepository.save(newObj);
	}

	public Cliente update(Integer id, ClienteDTO newObjDTO) {
		//é necessário definir como id o id que veio como parâmetro, pois é possível vir um id na url
		//e outro no objeto que vem no corpo da requisição; isso gera uma falha de segurança que deve ser evitada
		newObjDTO.setId(id);
		newObjDTO.setSenha(encoder.encode(newObjDTO.getSenha()));
		//se id não existe no banco, lança ObjectNotFoundException
		Cliente oldObj = findById(id);
		validaCpfAndEmail(newObjDTO);
		oldObj = new Cliente(newObjDTO);
		return clienteRepository.save(oldObj);
	}
	
	public void delete(Integer id) {
		//se id não existe no banco, lança ObjectNotFoundException
		Cliente obj = findById(id);
		
		//verifica se existem orderns de serviço em nome do técnico
		if (obj.getChamados().size() > 0) 
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço em seu nome e não pode ser excluído!");
		
		clienteRepository.deleteById(id);		
	}

	private void validaCpfAndEmail(ClienteDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId())
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId())
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
	}
	
}