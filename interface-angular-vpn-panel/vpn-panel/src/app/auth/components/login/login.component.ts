import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  template: `
    <div class="login-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>VPN Panel</mat-card-title>
          <mat-card-subtitle>Faça login para continuar</mat-card-subtitle>
        </mat-card-header>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <mat-card-content>
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Usuário</mat-label>
              <input matInput formControlName="usuario" required>
              <mat-error *ngIf="loginForm.get('usuario')?.hasError('required')">
                Usuário é obrigatório
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Senha</mat-label>
              <input matInput type="password" formControlName="senha" required>
              <mat-error *ngIf="loginForm.get('senha')?.hasError('required')">
                Senha é obrigatória
              </mat-error>
            </mat-form-field>
          </mat-card-content>

          <mat-card-actions align="end">
            <button mat-raised-button color="primary" type="submit" [disabled]="loginForm.invalid || loading">
              <mat-icon *ngIf="!loading">login</mat-icon>
              <mat-spinner diameter="20" *ngIf="loading"></mat-spinner>
              Login
            </button>
          </mat-card-actions>
        </form>
      </mat-card>
    </div>
  `,
  styles: [`
    .login-container {
      height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f5f5f5;
    }

    mat-card {
      width: 100%;
      max-width: 400px;
      margin: 20px;
    }

    .full-width {
      width: 100%;
      margin-bottom: 16px;
    }

    mat-card-actions {
      padding: 16px;
    }

    button {
      min-width: 120px;
    }

    mat-spinner {
      display: inline-block;
      margin-right: 8px;
    }
  `]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  returnUrl: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.formBuilder.group({
      usuario: ['', Validators.required],
      senha: ['', Validators.required]
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    const { usuario, senha } = this.loginForm.value;

    this.authService.login(usuario, senha).subscribe({
      next: () => {
        this.router.navigate([this.returnUrl]);
      },
      error: (error) => {
        this.snackBar.open(
          error.error?.message || 'Erro ao fazer login. Tente novamente.',
          'Fechar',
          { duration: 5000 }
        );
        this.loading = false;
      }
    });
  }
} 