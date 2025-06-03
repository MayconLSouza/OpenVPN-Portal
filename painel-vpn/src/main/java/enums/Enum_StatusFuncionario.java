package enums;

public enum Enum_StatusFuncionario {
	ATIVO(1),
	BLOQUEADO(2),
	REVOGADO(3);
	
	private final int status;
	
	Enum_StatusFuncionario(int status){
		this.status = status;
	}
	
	public int status() {
		return status;
	}
}
