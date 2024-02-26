package com.rodrigo.helpdesk.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TestaPerfil {

    @Test
    void assignPerfil_WithValidConstant_AssignsPerfil() {
        Perfil pAdmin = Perfil.ADMIN;
        Perfil pCliente = Perfil.CLIENTE;
        Perfil pTecnico = Perfil.TECNICO;

        assertThat(pAdmin.getCodigo()).isEqualTo(0);
        assertThat(pAdmin.getDescricao()).isEqualTo("ROLE_ADMIN");

        assertThat(pCliente.getCodigo()).isEqualTo(1);
        assertThat(pCliente.getDescricao()).isEqualTo("ROLE_CLIENTE");

        assertThat(pTecnico.getCodigo()).isEqualTo(2);
        assertThat(pTecnico.getDescricao()).isEqualTo("ROLE_TECNICO");
    }

    @Test
    void assignPerfil_WithValidCode_AssignsPerfil() {
        Perfil p0 = Perfil.toEnum(0);
        Perfil p1 = Perfil.toEnum(1);
        Perfil p2 = Perfil.toEnum(2);

        assertThat(p0.getCodigo()).isEqualTo(0);
        assertThat(p0.getDescricao()).isEqualTo("ROLE_ADMIN");

        assertThat(p1.getCodigo()).isEqualTo(1);
        assertThat(p1.getDescricao()).isEqualTo("ROLE_CLIENTE");

        assertThat(p2.getCodigo()).isEqualTo(2);
        assertThat(p2.getDescricao()).isEqualTo("ROLE_TECNICO");
    }

    @Test
    void assignPerfil_WithNullCode_AssingsNull() {
        assertThat(Perfil.toEnum(null)).isNull();
    }

    @Test
    void assignPerfil_WithInvalidCode_ThrowsException() {
        assertThatThrownBy(() -> Perfil.toEnum(3)).isInstanceOf(IllegalArgumentException.class);
    }

}
