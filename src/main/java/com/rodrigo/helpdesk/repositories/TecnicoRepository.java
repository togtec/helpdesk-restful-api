package com.rodrigo.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.helpdesk.model.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

}
