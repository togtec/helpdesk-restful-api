package com.rodrigo.helpdesk.common;

import com.rodrigo.helpdesk.enums.Prioridade;
import com.rodrigo.helpdesk.enums.Status;
import com.rodrigo.helpdesk.model.Chamado;

import static com.rodrigo.helpdesk.common.ClienteConstants.CLIENTE;
import static com.rodrigo.helpdesk.common.TecnicoContants.TECNICO;

public class ChamadoConstants {
    public static final Chamado CHAMADO = new Chamado(null, Prioridade.BAIXA, Status.ABERTO, "Novo chamado",
            "Observações do novo Chamado", CLIENTE, TECNICO);
    public static final Chamado INVALID_CHAMADO = new Chamado(null, null, null, "", "", null, null);
}
