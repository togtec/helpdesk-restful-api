package com.rodrigo.helpdesk;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rodrigo.helpdesk.domain.Chamado;
import com.rodrigo.helpdesk.domain.Cliente;
import com.rodrigo.helpdesk.domain.Tecnico;
import com.rodrigo.helpdesk.domain.enums.Perfil;
import com.rodrigo.helpdesk.domain.enums.Prioridade;
import com.rodrigo.helpdesk.domain.enums.Status;
import com.rodrigo.helpdesk.repositories.ChamadoRepository;
import com.rodrigo.helpdesk.repositories.ClienteRepository;
import com.rodrigo.helpdesk.repositories.TecnicoRepository;

@SpringBootApplication
public class HelpDeskApplication implements CommandLineRunner {
	@Autowired
	private TecnicoRepository tecnicoRepository;	
	@Autowired
	private ClienteRepository clienteRepository;	
	@Autowired
	private ChamadoRepository chamadoRepository;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Tecnico tec1 = new Tecnico(null, "Valdir Cezar", "19482233255", "valdir@mail.com", "admin");
		tec1.addPerfil(Perfil.ADMIN);
		
		Cliente cli1 = new Cliente(null, "Linus Torvalds", "77252642801", "torvalds@mail.com", "admin");
		
		Chamado chamado1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro Chamado", cli1, tec1);
		
		
		tecnicoRepository.saveAll(Arrays.asList(tec1));
		clienteRepository.saveAll(Arrays.asList(cli1));
		chamadoRepository.saveAll(Arrays.asList(chamado1));
	}
}
