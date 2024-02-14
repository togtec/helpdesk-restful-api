package com.rodrigo.helpdesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.helpdesk.dtos.ChamadoDTO;
import com.rodrigo.helpdesk.dtos.ClienteRequestDTO;
import com.rodrigo.helpdesk.dtos.TecnicoRequestDTO;
import com.rodrigo.helpdesk.enums.Perfil;
import com.rodrigo.helpdesk.enums.Prioridade;
import com.rodrigo.helpdesk.enums.Status;
import com.rodrigo.helpdesk.model.Chamado;
import com.rodrigo.helpdesk.model.Cliente;
import com.rodrigo.helpdesk.model.Tecnico;

@Service
public class DBService {

  @Autowired
  private TecnicoService tecnicoService;
  @Autowired
  private ClienteService clienteService;
  @Autowired
  private ChamadoService chamadoService;
	
	
	public void instanciaDB() {
    TecnicoRequestDTO tec1DTO = new TecnicoRequestDTO(new Tecnico(null, "Rodrigo Tognetta", "550.482.150-95", "tog@gmail.com", "1234"));
    tec1DTO.addPerfil(Perfil.ADMIN);
    Tecnico tec1 = tecnicoService.create(tec1DTO);
    Tecnico tec2 = tecnicoService.create(new TecnicoRequestDTO(new Tecnico(null, "Priscila Lopez", "903.347.070-56", "prilopez@gmail.com", "1234")));
    Tecnico tec3 = tecnicoService.create(new TecnicoRequestDTO(new Tecnico(null, "Alexandre Sanches", "271.068.470-54", "alesanches@gmail.com", "1234")));
    tecnicoService.create(new TecnicoRequestDTO(new Tecnico(null, "Mara Cruz", "162.720.120-39", "maracruz@gmail.com", "1234")));
    tecnicoService.create(new TecnicoRequestDTO(new Tecnico(null, "Alfredo Costa", "778.556.170-27", "alfcosta@gmail.com", "1234")));

    Cliente cli1 = clienteService.create(new ClienteRequestDTO(new Cliente(null, "Alessandra Lima", "111.661.890-74", "alelima@gmail.com", "1234")));
    Cliente cli2 = clienteService.create(new ClienteRequestDTO(new Cliente(null, "Flavio Sampaio", "322.429.140-06", "flaviosp@bol.com.br", "1234")));
    Cliente cli3 = clienteService.create(new ClienteRequestDTO(new Cliente(null, "Igor Batista", "792.043.830-62", "ibatista@bol.com.br", "1234")));
    clienteService.create(new ClienteRequestDTO(new Cliente(null, "Paulo Cintra", "177.409.680-30", "paulo.cintra@globo.com", "1234")));
    clienteService.create(new ClienteRequestDTO(new Cliente(null, "Sabrina Coral", "081.399.300-83", "sabcoral@gmail.com", "1234")));
				
		Chamado cham1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Título chamado 1", "Observações chamado 1", cli1, tec1);
		Chamado cham2 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Título chamado 2", "Observações chamado 2", cli2, tec1);
		Chamado cham3 = new Chamado(null, Prioridade.BAIXA, Status.ENCERRADO, "Título chamado 3", "Observações chamado 3", cli3, tec2);
		Chamado cham4 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Título chamado 4", "Observações chamado 4", cli3, tec3);
		Chamado cham5 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Título chamado 5", "Observações chamado 5", cli1, tec2);
		Chamado cham6 = new Chamado(null, Prioridade.BAIXA, Status.ENCERRADO, "Título chamado 6", "Observações chamado 6", cli2, tec1);
    chamadoService.create(new ChamadoDTO(cham1));
    chamadoService.create(new ChamadoDTO(cham2));
    chamadoService.create(new ChamadoDTO(cham3));
    chamadoService.create(new ChamadoDTO(cham4));
    chamadoService.create(new ChamadoDTO(cham5));
    chamadoService.create(new ChamadoDTO(cham6));
	}

}
