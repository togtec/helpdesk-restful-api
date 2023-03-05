package com.rodrigo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.helpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
}
