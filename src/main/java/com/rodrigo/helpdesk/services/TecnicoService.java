package com.rodrigo.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		//return obj.orElse(null);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
		
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {
		/* vamos deixar o id nulo porque se houver um valor para id na requisição, 
		o método save fará um update ao invés de salvar */
		objDTO.setId(null); 
		validaCpfAndEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return tecnicoRepository.save(newObj);
	}

	private void validaCpfAndEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId())
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId())
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
	}

	public Tecnico update(Integer id, TecnicoDTO objDTO) {
		//é necessário definir como id o id que veio como parâmetro, pois é possível vir um id na url
		//e outro no objeto que vem no corpo da requisição; isso gera uma falha de segurança que deve ser evitada
		objDTO.setId(id);
		Tecnico oldObj = findById(id);
		validaCpfAndEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		return tecnicoRepository.save(oldObj);
	}
	
}