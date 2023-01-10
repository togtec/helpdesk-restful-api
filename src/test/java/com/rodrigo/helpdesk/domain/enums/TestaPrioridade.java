package com.rodrigo.helpdesk.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestaPrioridade {

	@Test
	void testa_gets() {
		Prioridade prioridadeBaixa = Prioridade.BAIXA;
		assertEquals(0, prioridadeBaixa.getCodigo());
		assertEquals("BAIXA", prioridadeBaixa.getDescricao());
		
		Prioridade prioridadeMedia = Prioridade.MEDIA;
		assertEquals(1, prioridadeMedia.getCodigo());
		assertEquals("MEDIA", prioridadeMedia.getDescricao());
		
		Prioridade prioridadeAlta = Prioridade.ALTA;
		assertEquals(2, prioridadeAlta.getCodigo());
		assertEquals("ALTA", prioridadeAlta.getDescricao());
	}

	@Test
	void testa_toEnum() {
		Prioridade prioridade0 = Prioridade.toEnum(0);
		assertEquals(0, prioridade0.getCodigo());
		assertEquals("BAIXA", prioridade0.getDescricao());
		
		Prioridade prioridade1 = Prioridade.toEnum(1);
		assertEquals(1, prioridade1.getCodigo());
		assertEquals("MEDIA", prioridade1.getDescricao());
		
		Prioridade prioridade2 = Prioridade.toEnum(2);
		assertEquals(2, prioridade2.getCodigo());
		assertEquals("ALTA", prioridade2.getDescricao());
		
		/* código nulo retorna nulo */
		assertNull(Prioridade.toEnum(null));
		
		/* código inexistente lança exeção*/
		assertThrows(IllegalArgumentException.class, () -> Prioridade.toEnum(3));			
	}
}
