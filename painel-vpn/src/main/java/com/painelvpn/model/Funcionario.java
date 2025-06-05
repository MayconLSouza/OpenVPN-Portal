package com.painelvpn.model;

import java.util.ArrayList;
import java.util.List;

import com.painelvpn.enums.Enum_StatusFuncionario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String idFuncionario;

	@NotBlank(message = "O nome é obrigatório")
	@Column(nullable = false)
	private String nome;

	@Email(message = "Email inválido")
	@NotBlank(message = "O email é obrigatório")
	@Column(unique = true, nullable = false)
	private String email;

	@NotBlank(message = "O usuário é obrigatório")
	@Column(unique = true, nullable = false)
	private String usuario;
	
	@NotBlank(message = "A senha é obrigatória")
	@Column(nullable = false)
	private String senha;
	
	@Column(nullable = false)
	private boolean isAdmin;

	@Column(nullable = false)
	private int tentativasLogin;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Enum_StatusFuncionario status;
	
	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Certificado> certificados = new ArrayList<>();
	
	public Funcionario() {
		this.tentativasLogin = 0;
		this.status = Enum_StatusFuncionario.ATIVO;
		this.isAdmin = false;
	}

	// Getters e Setters
	public String getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(String idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getTentativasLogin() {
		return tentativasLogin;
	}

	public void setTentativasLogin(int tentativasLogin) {
		this.tentativasLogin = tentativasLogin;
	}

	public Enum_StatusFuncionario getStatus() {
		return status;
	}

	public void setStatus(Enum_StatusFuncionario status) {
		this.status = status;
	}

	public List<Certificado> getCertificados() {
		return certificados;
	}

	public void setCertificados(List<Certificado> certificados) {
		this.certificados = certificados;
	}

	// Métodos de negócio
	public void incrementarTentativasLogin() {
		this.tentativasLogin++;
		if (this.tentativasLogin >= 3) {
			this.status = Enum_StatusFuncionario.BLOQUEADO;
		}
	}

	public void resetarTentativasLogin() {
		this.tentativasLogin = 0;
	}

	// Métodos helper para gerenciar a relação bidirecional
	public void adicionarCertificado(Certificado certificado) {
		certificados.add(certificado);
		certificado.setFuncionario(this);
	}

	public void removerCertificado(Certificado certificado) {
		certificados.remove(certificado);
		certificado.setFuncionario(null);
	}
}
