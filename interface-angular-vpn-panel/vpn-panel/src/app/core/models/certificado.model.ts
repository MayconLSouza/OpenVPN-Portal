import { Funcionario } from './funcionario.model';

export interface Certificado {
  id: string;
  dataCriacao: Date;
  dataValidade: Date;
  caminhoLinux: string;
  funcionario: Funcionario;
} 