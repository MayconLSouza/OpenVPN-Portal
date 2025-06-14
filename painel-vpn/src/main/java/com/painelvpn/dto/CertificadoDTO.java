package com.painelvpn.dto;

import java.time.LocalDate;
import com.painelvpn.model.Certificado;

public class CertificadoDTO {
    private String id;
    private LocalDate dataCriacao;
    private LocalDate dataValidade;
    private String caminhoLinux;
    private String funcionarioId;

    public CertificadoDTO(Certificado certificado) {
        this.id = certificado.getId();
        this.dataCriacao = certificado.getDataCriacao();
        this.dataValidade = certificado.getDataValidade();
        this.caminhoLinux = certificado.getCaminhoLinux();
        this.funcionarioId = certificado.getFuncionario().getIdFuncionario();
    }

    // Getters
    public String getId() {
        return id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public String getCaminhoLinux() {
        return caminhoLinux;
    }

    public String getFuncionarioId() {
        return funcionarioId;
    }
} 