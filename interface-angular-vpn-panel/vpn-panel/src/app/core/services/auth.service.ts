import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Funcionario } from '../models/funcionario.model';
import { CookieService } from 'ngx-cookie-service';

interface AuthResponse {
  token: string;
  funcionario: Funcionario;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<Funcionario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private cookieService: CookieService
  ) {
    this.checkAuthStatus();
  }

  login(usuario: string, senha: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, { usuario, senha })
      .pipe(
        tap(response => {
          this.cookieService.set('jwt_token', response.token, { secure: true, sameSite: 'Strict' });
          this.checkAuthStatus();
        })
      );
  }

  logout(): void {
    this.cookieService.delete('jwt_token');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return this.cookieService.get('jwt_token') || null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const currentUser = this.currentUserSubject.value;
    return currentUser?.isAdmin || false;
  }

  getCurrentUser(): Funcionario | null {
    return this.currentUserSubject.value;
  }

  private checkAuthStatus(): void {
    const token = this.getToken();
    if (token) {
      this.http.get<Funcionario>(`${environment.apiUrl}/auth/me`)
        .subscribe({
          next: (user) => {
            console.log('Usuário autenticado:', user);
            if (user && user.status === 'ATIVO') {
              this.currentUserSubject.next(user);
            } else {
              this.logout();
            }
          },
          error: () => this.logout()
        });
    }
  }
} 