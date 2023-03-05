package com.rodrigo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
	
}
