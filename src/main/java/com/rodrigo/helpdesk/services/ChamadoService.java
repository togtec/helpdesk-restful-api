package com.rodrigo.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.domain.Chamado;
import com.rodrigo.helpdesk.domain.Cliente;
import com.rodrigo.helpdesk.domain.Tecnico;
import com.rodrigo.helpdesk.domain.dtos.ChamadoDTO;
import com.rodrigo.helpdesk.domain.enums.Prioridade;
import com.rodrigo.helpdesk.domain.enums.Status;
import com.rodrigo.helpdesk.repositories.ChamadoRepository;
import com.rodrigo.helpdesk.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository repository;
	@Autowired
	private TecnicoService tecnicoService;
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Integer id) {
		Optional<Chamado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID:" + id));
	}
	
	public List<Chamado> findAll() {
		return repository.findAll();
	}

	public Chamado create(@Valid ChamadoDTO objDTO) {
		/* definimos id null pois se houver um id na requisição, o método save fará update ao invés de salvar */
		objDTO.setId(null);
		return repository.save(newChamado(objDTO));
	}
	
	public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
		//é necessário definir como id o id que veio como parâmetro, pois é possível vir um id na url
		//e outro no objeto que vem no corpo da requisição; isso gera uma falha de segurança que deve ser evitada
		objDTO.setId(id);
		
		//se id não existe no banco, lança ObjectNotFoundException
		Chamado oldObj = findById(id);
		
		oldObj = newChamado(objDTO);
		return repository.save(oldObj);
	}	
	
	private Chamado newChamado(ChamadoDTO objDTO) {
		//se tecnico ou cliente não existirem, lança exceção
		Tecnico tecnico = tecnicoService.findById(objDTO.getTecnico());
		Cliente cliente = clienteService.findById(objDTO.getCliente());
		
		Chamado chamado = new Chamado();
		
		if (objDTO.getId() != null) { //apenas atualizar
			chamado.setId(objDTO.getId());
		}
		
		if (objDTO.getStatus().equals(2)) { //status 2 means ENCERRADO
			chamado.setDataFechamento(LocalDate.now());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
		chamado.setStatus(Status.toEnum(objDTO.getStatus()));
		chamado.setTitulo(objDTO.getTitulo());
		chamado.setObservacoes(objDTO.getObservacoes());
		return chamado;
	}

}
