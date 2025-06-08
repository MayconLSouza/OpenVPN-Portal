package com.painelvpn.dto;

import com.painelvpn.validation.SenhaForte;
import jakarta.validation.constraints.NotBlank;

public class RedefinicaoSenhaRequest {
    @NotBlank(message = "O token é obrigatório")
    private String token;

    @NotBlank(message = "A nova senha é obrigatória")
    @SenhaForte
    private String novaSenha;

    @NotBlank(message = "A confirmação da senha é obrigatória")
    private String confirmacaoSenha;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
} 