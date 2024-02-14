package com.rodrigo.helpdesk.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class TestaStatus {
	
	@Test
	void assignStatus_WithValidConstant_assignsStatus() {
		Status stAberto = Status.ABERTO;
		Status stAndamento = Status.ANDAMENTO;
		Status stEncerrado = Status.ENCERRADO;

		assertThat(stAberto.getCodigo()).isEqualTo(0);
		assertThat(stAberto.getDescricao()).isEqualTo("ABERTO");

		assertThat(stAndamento.getCodigo()).isEqualTo(1);
		assertThat(stAndamento.getDescricao()).isEqualTo("ANDAMENTO");

		assertThat(stEncerrado.getCodigo()).isEqualTo(2);
		assertThat(stEncerrado.getDescricao()).isEqualTo("ENCERRADO");
	}

	@Test
	void assignStatus_WithValidCode_assignsStatus() {
		Status st0 = Status.toEnum(0);
		Status st1 = Status.toEnum(1);
		Status st2 = Status.toEnum(2);

		assertThat(st0.getCodigo()).isEqualTo(0);
		assertThat(st0.getDescricao()).isEqualTo("ABERTO");

		assertThat(st1.getCodigo()).isEqualTo(1);
		assertThat(st1.getDescricao()).isEqualTo("ANDAMENTO");

		assertThat(st2.getCodigo()).isEqualTo(2);
		assertThat(st2.getDescricao()).isEqualTo("ENCERRADO");
	}

	@Test
	void assignStatus_WithNullCode_assignsNull() {
		assertThat(Status.toEnum(null)).isEqualTo(null);
	}

	@Test
	void assignStatus_WithInvalideCode_ThrowsException() {
		assertThatThrownBy(() -> Status.toEnum(10)).isInstanceOf(IllegalArgumentException.class);
	}
  
}
