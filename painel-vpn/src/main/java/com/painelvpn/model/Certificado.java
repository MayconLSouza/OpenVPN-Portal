package com.painelvpn.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Certificado {

    @Id
    @Column(length = 7)
    @Size(min = 7, max = 7, message = "O ID deve ter exatamente 7 caracteres")
    private String id;

    @NotNull(message = "A data de criação não pode ser nula")
    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @NotNull(message = "A data de validade não pode ser nula")
    @Future(message = "A data de validade deve ser uma data futura")
    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @NotBlank(message = "O caminho do arquivo não pode estar em branco")
    @Column(name = "caminho_linux")
    private String caminhoLinux;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_funcionario", nullable = false)
    private Funcionario funcionario;

    // Construtor vazio
    public Certificado() {
        super();
    }

    // Construtor com parâmetros
    public Certificado(LocalDate dataCriacao, LocalDate dataValidade, String caminhoLinux, Funcionario funcionario) {
        this.dataCriacao = dataCriacao;
        this.dataValidade = dataValidade;
        this.caminhoLinux = caminhoLinux;
        this.funcionario = funcionario;
    }

    @PrePersist
    public void gerarId() {
        if (this.id == null) {
            this.id = UUID.randomUUID()
                         .toString()
                         .replaceAll("-", "")
                         .substring(0, 7)
                         .toUpperCase();
        }
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getCaminhoLinux() {
        return caminhoLinux;
    }

    public void setCaminhoLinux(String caminhoLinux) {
        this.caminhoLinux = caminhoLinux;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    // ToString
    @Override
    public String toString() {
        return "Certificado{" +
                "id=" + id +
                ", dataCriacao=" + dataCriacao +
                ", dataValidade=" + dataValidade +
                ", caminhoLinux='" + caminhoLinux + '\'' +
                ", funcionario=" + funcionario +
                '}';
    }
}
