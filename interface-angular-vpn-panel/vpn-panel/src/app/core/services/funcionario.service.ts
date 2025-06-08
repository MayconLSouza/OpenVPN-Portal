import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Funcionario {
  id: string;
  nome: string;
  email: string;
  usuario: string;
  cargo: string;
  admin: boolean;
  ativo: boolean;
}

export interface FuncionarioCreate {
  nome: string;
  email: string;
  usuario: string;
  senha: string;
  cargo: string;
  admin: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  private apiUrl = `${environment.apiUrl}/funcionarios`;

  constructor(private http: HttpClient) { }

  listar(): Observable<Funcionario[]> {
    return this.http.get<Funcionario[]>(this.apiUrl);
  }

  cadastrarFuncionario(funcionario: FuncionarioCreate): Observable<Funcionario> {
    return this.http.post<Funcionario>(this.apiUrl, funcionario);
  }

  elegerAdministrador(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/eleger-admin`, {});
  }

  revogarAdministrador(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/revogar-admin`, {});
  }

  revogarAcesso(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/revogar-acesso`, {});
  }

  reativarAcesso(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/reativar-acesso`, {});
  }

  remover(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
} 