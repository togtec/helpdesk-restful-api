package com.rodrigo.helpdesk.common;

import com.rodrigo.helpdesk.model.Tecnico;

public class TecnicoContants {
  public static final Tecnico TECNICO = new Tecnico(null, "nome", "523.213.260-18", "nome@email.com", "password");
  public static final Tecnico INVALID_TECNICO = new Tecnico(null, "", "", "", "");
}
