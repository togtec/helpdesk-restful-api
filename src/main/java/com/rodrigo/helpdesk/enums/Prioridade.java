package com.rodrigo.helpdesk.enums;

public enum Prioridade {
	BAIXA(0, "BAIXA"),
	MEDIA(1, "MEDIA"),
	ALTA(2, "ALTA");
	
	private Integer codigo;
	private String descricao;
	
	private Prioridade(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Prioridade toEnum(Integer cod) {
		if (cod == null) return null;
		
		for(Prioridade p : Prioridade.values()) {
			if (p.getCodigo() == cod) return p;
		}
		
		throw new IllegalArgumentException("Prioridade inválida");
	}
}
