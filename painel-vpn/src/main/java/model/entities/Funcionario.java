package model.entities;

import java.util.List;

import enums.Enum_StatusFuncionario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Funcionario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique=true, length = 30)
	private String usuario;
	
	@Column(nullable = false)
	private String senha;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private Enum_StatusFuncionario status;
	
	@Column(nullable = false)
	private int tentativasLogin;
	
	@Column(nullable = false)
	private boolean isAdmin;
	
	private List<Certificado> certificado;
	
	public Funcionario() {
		super();
	}
	

}
