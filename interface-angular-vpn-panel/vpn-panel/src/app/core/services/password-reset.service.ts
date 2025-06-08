import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PasswordResetService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  requestPasswordReset(username: string): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/solicitar-redefinicao`, { usuario: username });
  }

  resetPassword(token: string, newPassword: string, confirmPassword: string): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/redefinir`, {
      token,
      novaSenha: newPassword,
      confirmacaoSenha: confirmPassword
    });
  }
} 