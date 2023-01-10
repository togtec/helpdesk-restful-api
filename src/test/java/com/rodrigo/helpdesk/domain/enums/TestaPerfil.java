package com.rodrigo.helpdesk.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestaPerfil {

	@Test
	void testa_gets() {
		Perfil perfilAdmin = Perfil.ADMIN;
		assertEquals(0, perfilAdmin.getCodigo());
		assertEquals("ROLE_ADMIN", perfilAdmin.getDescricao());
		
		Perfil perfilCliente = Perfil.CLIENTE;
		assertEquals(1, perfilCliente.getCodigo());
		assertEquals("ROLE_CLIENTE", perfilCliente.getDescricao());
		
		Perfil perfilTecnico = Perfil.TECNICO;
		assertEquals(2, perfilTecnico.getCodigo());
		assertEquals("ROLE_TECNICO", perfilTecnico.getDescricao());
	}

	@Test
	void testa_toEnum() {
		Perfil perfil0 = Perfil.toEnum(0);
		assertEquals(0, perfil0.getCodigo());
		assertEquals("ROLE_ADMIN", perfil0.getDescricao());
		
		Perfil perfil1 = Perfil.toEnum(1);
		assertEquals(1, perfil1.getCodigo());
		assertEquals("ROLE_CLIENTE", perfil1.getDescricao());
		
		Perfil perfil2 = Perfil.toEnum(2);
		assertEquals(2, perfil2.getCodigo());
		assertEquals("ROLE_TECNICO", perfil2.getDescricao());
		
		/* código nulo retorna nulo */
		assertNull(Perfil.toEnum(null));
		
		/* código inexistente lança exeção*/
		assertThrows(IllegalArgumentException.class, () -> Perfil.toEnum(3));	
	}
	
}
