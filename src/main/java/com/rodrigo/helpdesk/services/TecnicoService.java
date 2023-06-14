package com.rodrigo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.domain.Pessoa;
import com.rodrigo.helpdesk.domain.Tecnico;
import com.rodrigo.helpdesk.domain.dtos.TecnicoDTO;
import com.rodrigo.helpdesk.repositories.PessoaRepository;
import com.rodrigo.helpdesk.repositories.TecnicoRepository;
import com.rodrigo.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.rodrigo.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired	
	private TecnicoRepository tecnicoRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;	
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
		
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {
		/* definimos id null pois se houver um id na requisição, o método save fará update ao invés de salvar */
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaCpfAndEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return tecnicoRepository.save(newObj);
	}

	public Tecnico update(Integer id, TecnicoDTO newObjDTO) {
		//é necessário definir como id o id que veio como parâmetro, pois é possível vir um id na url
		//e outro no objeto que vem no corpo da requisição; isso gera uma falha de segurança que deve ser evitada
		newObjDTO.setId(id);
		newObjDTO.setSenha(encoder.encode(newObjDTO.getSenha()));
		//se id não existe no banco, lança ObjectNotFoundException
		Tecnico oldObj = findById(id);
		validaCpfAndEmail(newObjDTO);
		oldObj = new Tecnico(newObjDTO);
		return tecnicoRepository.save(oldObj);
	}
	
	public void delete(Integer id) {
		//se id não existe no banco, lança ObjectNotFoundException
		Tecnico obj = findById(id);
		
		//verifica se existem orderns de serviço em nome do técnico
		if (obj.getChamados().size() > 0) 
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço em seu nome e não pode ser excluído!");
		
		tecnicoRepository.deleteById(id);		
	}

	private void validaCpfAndEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId())
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId())
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
	}
	
}