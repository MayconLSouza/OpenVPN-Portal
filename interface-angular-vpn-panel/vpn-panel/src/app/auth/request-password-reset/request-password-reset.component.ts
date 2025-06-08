import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordResetService } from '../../core/services/password-reset.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-request-password-reset',
  template: `
    <div class="container">
      <mat-card class="password-reset-card">
        <mat-card-header>
          <mat-card-title>Recuperação de Senha</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="requestForm" (ngSubmit)="onSubmit()">
            <mat-form-field appearance="fill">
              <mat-label>Nome de Usuário</mat-label>
              <input matInput formControlName="username" required>
              <mat-error *ngIf="requestForm.get('username')?.hasError('required')">
                Nome de usuário é obrigatório
              </mat-error>
            </mat-form-field>
            
            <div class="button-container">
              <button mat-button type="button" (click)="goBack()">Voltar</button>
              <button mat-raised-button color="primary" type="submit" [disabled]="requestForm.invalid">
                Enviar
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .container {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      background-color: #f5f5f5;
    }
    .password-reset-card {
      min-width: 300px;
      padding: 20px;
    }
    mat-form-field {
      width: 100%;
      margin-bottom: 20px;
    }
    .button-container {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    }
  `]
})
export class RequestPasswordResetComponent {
  requestForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private passwordResetService: PasswordResetService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.requestForm = this.fb.group({
      username: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.requestForm.valid) {
      const username = this.requestForm.get('username')?.value;
      this.passwordResetService.requestPasswordReset(username).subscribe({
        next: (response) => {
          this.snackBar.open(response, 'OK', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          });
          this.router.navigate(['/reset-password']);
        },
        error: (error) => {
          this.snackBar.open('Erro ao solicitar redefinição de senha. Tente novamente.', 'OK', {
            duration: 5000
          });
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['/login']);
  }
} 