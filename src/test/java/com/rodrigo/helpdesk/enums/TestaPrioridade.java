package com.rodrigo.helpdesk.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TestaPrioridade {

	@Test
	void assignPrioridade_WithValidConstant_AssignsPrioridade() {
		Prioridade pBaixa = Prioridade.BAIXA;
		Prioridade pMedia = Prioridade.MEDIA;
		Prioridade pAlta = Prioridade.ALTA;
		
		assertThat(pBaixa.getCodigo()).isEqualTo(0);
		assertThat(pBaixa.getDescricao()).isEqualTo("BAIXA");
				
		assertThat(pMedia.getCodigo()).isEqualTo(1);
		assertThat(pMedia.getDescricao()).isEqualTo("MEDIA");
				
		assertThat(pAlta.getCodigo()).isEqualTo(2);
		assertThat(pAlta.getDescricao()).isEqualTo("ALTA");
	}

	@Test
	void assignPrioridade_WithValidCode_AssignsPrioridade() {
		Prioridade p0 = Prioridade.toEnum(0);
		Prioridade p1 = Prioridade.toEnum(1);
		Prioridade p2 = Prioridade.toEnum(2);
		
		assertThat(p0.getCodigo()).isEqualTo(0);
		assertThat(p0.getDescricao()).isEqualTo("BAIXA");
				
		assertThat(p1.getCodigo()).isEqualTo(1);
		assertThat(p1.getDescricao()).isEqualTo("MEDIA");
				
		assertThat(p2.getCodigo()).isEqualTo(2);
		assertThat(p2.getDescricao()).isEqualTo("ALTA");			
	}
	
	@Test
	void assignPrioridade_WithNullCode_AssignsNull() {
		assertThat(Prioridade.toEnum(null)).isEqualTo(null);
	}
	
	@Test
	void assignPrioridade_WithInvalidCode_ThrowsException() {
		assertThatThrownBy(() -> Prioridade.toEnum(3)).isInstanceOf(IllegalArgumentException.class);
	}
}
