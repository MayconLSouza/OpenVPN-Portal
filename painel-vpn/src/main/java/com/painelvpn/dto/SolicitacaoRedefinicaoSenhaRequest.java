package com.painelvpn.dto;

import jakarta.validation.constraints.NotBlank;

public class SolicitacaoRedefinicaoSenhaRequest {
    @NotBlank(message = "O username é obrigatório")
    private String usuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
} 