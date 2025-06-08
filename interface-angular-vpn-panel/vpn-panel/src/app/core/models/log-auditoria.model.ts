export interface LogAuditoria {
  id: string;
  acao: string;
  detalhes: string;
  idFuncionario: string;
  nomeFuncionario: string;
  dataHora: Date;
  ip: string;
} 