package com.rodrigo.helpdesk.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestaStatus {

	@Test
	void testa_gets() {
		Status statusAberto = Status.ABERTO;
		assertEquals(0, statusAberto.getCodigo());
		assertEquals("ABERTO", statusAberto.getDescricao());
		
		Status statusAndamento = Status.ANDAMENTO;
		assertEquals(1, statusAndamento.getCodigo());
		assertEquals("ANDAMENTO", statusAndamento.getDescricao());
		
		Status statusEncerrado = Status.ENCERRADO;
		assertEquals(2, statusEncerrado.getCodigo());
		assertEquals("ENCERRADO", statusEncerrado.getDescricao());
	}

	@Test
	void testa_toEnum() {
		Status status0 = Status.toEnum(0);
		assertEquals(0, status0.getCodigo());
		assertEquals("ABERTO", status0.getDescricao());
		
		Status status1 = Status.toEnum(1);
		assertEquals(1, status1.getCodigo());
		assertEquals("ANDAMENTO", status1.getDescricao());
		
		Status status2 = Status.toEnum(2);
		assertEquals(2, status2.getCodigo());
		assertEquals("ENCERRADO", status2.getDescricao());
		
		/* código nulo retorna nulo */
		assertNull(Status.toEnum(null));
		
		/* código inexistente lança exeção*/
		assertThrows(IllegalArgumentException.class, () -> Status.toEnum(3));			
	}
}
