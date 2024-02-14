package com.rodrigo.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.dtos.ChamadoDTO;
import com.rodrigo.helpdesk.enums.Prioridade;
import com.rodrigo.helpdesk.enums.Status;
import com.rodrigo.helpdesk.exceptions.ObjectNotFoundException;
import com.rodrigo.helpdesk.model.Chamado;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.model.Tecnico;
import com.rodrigo.helpdesk.repositories.ChamadoRepository;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository chamadoRepository;
	@Autowired
	private TecnicoService tecnicoService;
	@Autowired
	private ClienteService clienteService;



  public Chamado create(ChamadoDTO objDTO) {
    /* id deve ser nulo - se houver um id na requisição, o método save fará um update ao invés de salvar */
		objDTO.setId(null);
    objDTO.setDataAbertura(LocalDate.now());
		return chamadoRepository.save(newChamado(objDTO));
	}
	
	public Chamado findById(Long id) {
		Optional<Chamado> obj = chamadoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID:" + id));
	}
	
	public List<Chamado> findAll() {
		return chamadoRepository.findAll();
	}
	
	public Chamado update(Long id, ChamadoDTO objDTO) {
		//define o id da url evitando falha de segurança caso haja outro id no objeto que vem no corpo da requisiação
		objDTO.setId(id);
		
		//se id não existe no banco, lança ObjectNotFoundException
		Chamado oldObj = findById(id);
    objDTO.setDataAbertura(oldObj.getDataAbertura());
		
		oldObj = newChamado(objDTO);
		return chamadoRepository.save(oldObj);
	}	
	
	private Chamado newChamado(ChamadoDTO objDTO) {
		//se tecnico ou cliente não existirem, lança exceção
    Cliente cliente = clienteService.findById(objDTO.getCliente());
		Tecnico tecnico = tecnicoService.findById(objDTO.getTecnico());
		
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
