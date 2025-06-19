package com.painelvpn.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Informe o username para realizar o processo de entrada")
    private String usuario;

    @NotBlank(message = "Informe o password do usuário para realizar o processo de entrada")
    private String senha;

    public LoginRequest() {
    }

    public LoginRequest(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
} 