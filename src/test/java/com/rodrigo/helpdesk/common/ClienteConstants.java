package com.rodrigo.helpdesk.common;

import com.rodrigo.helpdesk.model.Cliente;

public class ClienteConstants {
  public static final Cliente CLIENTE = new Cliente(null, "nome", "523.213.260-18", "nome@email.com", "password");
	public static final Cliente INVALID_CLIENTE = new Cliente(null, "", "", "", "");
}
