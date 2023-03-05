package com.rodrigo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
	
}
