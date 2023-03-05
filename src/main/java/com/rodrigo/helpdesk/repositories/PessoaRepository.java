package com.rodrigo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.helpdesk.domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
	
}
