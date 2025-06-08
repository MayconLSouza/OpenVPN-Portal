import { Certificado } from './certificado.model';

export interface Funcionario {
  idFuncionario: string;
  nome: string;
  email: string;
  usuario: string;
  senha: string;
  isAdmin: boolean;
  tentativasLogin: number;
  status: 'ATIVO' | 'BLOQUEADO';
  certificados: Certificado[];
}

export interface FuncionarioDTO extends Omit<Funcionario, 'senha' | 'certificados'> {
  certificados?: Certificado[];
} 