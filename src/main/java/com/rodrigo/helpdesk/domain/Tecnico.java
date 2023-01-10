package com.rodrigo.helpdesk.domain;

import java.util.ArrayList;
import java.util.List;

import com.rodrigo.helpdesk.domain.enums.Perfil;

public class Tecnico extends Pessoa {
	private List<Chamado> chamados = new ArrayList<>();

	public Tecnico() {
		super();
		super.addPerfil(Perfil.TECNICO);
	}

	public Tecnico(Integer id, String nome, String cpf, String email, String senha) {
		super(id, nome, cpf, email, senha);
		super.addPerfil(Perfil.TECNICO);
	}

	public List<Chamado> getChamados() {
		return chamados;
	}

	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}
	
}
