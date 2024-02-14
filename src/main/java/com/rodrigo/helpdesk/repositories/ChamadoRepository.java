package com.rodrigo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.helpdesk.model.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
	
}
