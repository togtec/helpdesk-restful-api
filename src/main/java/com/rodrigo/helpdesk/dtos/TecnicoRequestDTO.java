package com.rodrigo.helpdesk.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rodrigo.helpdesk.enums.Perfil;
import com.rodrigo.helpdesk.model.Tecnico;

import jakarta.validation.constraints.NotBlank;

public class TecnicoRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotBlank(message = "O campo NOME é de preenchimento obrigatório!")
    protected String nome;
    @CPF
    @NotBlank(message = "O campo CPF é de preenchimento obrigatório!")
    protected String cpf;
    @NotBlank(message = "O campo Email é de preenchimento obrigatório!")
    protected String email;
    @NotBlank(message = "O campo SENHA é de preenchimento obrigatório!")
    protected String senha;
    protected Set<Integer> perfis = new HashSet<>(); // HashSet por não permitir valores duplicados

    @JsonFormat(pattern = "dd/MM/yyyy")
    protected LocalDate dataCriacao;

    public TecnicoRequestDTO() {
        super();
        addPerfil(Perfil.CLIENTE);
        addPerfil(Perfil.TECNICO);
    }

    public TecnicoRequestDTO(Tecnico obj) {
        super();
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
        this.perfis = obj.getPerfis().stream().map(x -> x.getCodigo()).collect(Collectors.toSet());
        this.dataCriacao = obj.getDataCriacao();
        addPerfil(Perfil.CLIENTE);
        addPerfil(Perfil.TECNICO);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
    }

    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil.getCodigo());
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
