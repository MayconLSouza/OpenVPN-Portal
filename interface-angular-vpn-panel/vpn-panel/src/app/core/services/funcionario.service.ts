import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Funcionario {
  id: string;
  nome: string;
  email: string;
  usuario: string;
  admin: boolean;
  ativo: boolean;
  tentativasLogin: number;
  status: string;
}

export interface FuncionarioCreate {
  nome: string;
  email: string;
  usuario: string;
  senha: string;
  admin: boolean;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  private apiUrl = `${environment.apiUrl}/funcionarios`;

  constructor(private http: HttpClient) { }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro na operação';
    
    if (error.error instanceof ErrorEvent) {
      // Erro do cliente
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      // Erro do servidor
      if (error.status === 404) {
        errorMessage = 'Recurso não encontrado';
      } else if (error.status === 403) {
        errorMessage = 'Acesso negado';
      } else if (error.status === 400) {
        errorMessage = error.error?.message || 'Dados inválidos';
      } else if (error.status === 500) {
        errorMessage = 'Erro interno do servidor';
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }

  listar(): Observable<Funcionario[]> {
    return this.http.get<Funcionario[]>(this.apiUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  filtrar(termo: string): Observable<Funcionario[]> {
    let params = new HttpParams();
    if (termo) {
      params = params.set('filtro', termo);
    }
    return this.http.get<Funcionario[]>(this.apiUrl, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

  cadastrarFuncionario(funcionario: FuncionarioCreate): Observable<Funcionario> {
    return this.http.post<Funcionario>(this.apiUrl, funcionario)
      .pipe(
        map(response => {
          if (!response) {
            throw new Error('Resposta inválida do servidor');
          }
          return response;
        }),
        catchError(this.handleError)
      );
  }

  elegerAdministrador(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/eleger-admin`, {})
      .pipe(
        map(response => {
          if (!response) {
            throw new Error('Resposta inválida do servidor');
          }
          return response;
        }),
        catchError(this.handleError)
      );
  }

  revogarAdministrador(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/revogar-admin`, {})
      .pipe(
        map(response => {
          if (!response) {
            throw new Error('Resposta inválida do servidor');
          }
          return response;
        }),
        catchError(this.handleError)
      );
  }

  revogarAcesso(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/revogar-acesso`, {})
      .pipe(
        map(response => {
          if (!response) {
            throw new Error('Resposta inválida do servidor');
          }
          return response;
        }),
        catchError(this.handleError)
      );
  }

  reativarAcesso(id: string): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}/reativar-acesso`, {})
      .pipe(
        map(response => {
          if (!response) {
            throw new Error('Resposta inválida do servidor');
          }
          return response;
        }),
        catchError(this.handleError)
      );
  }

  removerFuncionario(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        map(() => void 0),
        catchError(this.handleError)
      );
  }
} 