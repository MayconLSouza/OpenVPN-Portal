import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordResetService } from '../../core/services/password-reset.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-reset-password',
  template: `
    <div class="container">
      <mat-card class="reset-password-card">
        <mat-card-header>
          <mat-card-title>Redefinir Senha</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="resetForm" (ngSubmit)="onSubmit()">
            <mat-form-field appearance="fill">
              <mat-label>Token</mat-label>
              <input matInput formControlName="token" required>
              <mat-error *ngIf="resetForm.get('token')?.hasError('required')">
                Token é obrigatório
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="fill">
              <mat-label>Nova Senha</mat-label>
              <input matInput [type]="hideNewPassword ? 'password' : 'text'" formControlName="newPassword" required>
              <button mat-icon-button matSuffix (click)="hideNewPassword = !hideNewPassword" type="button">
                <mat-icon>{{hideNewPassword ? 'visibility_off' : 'visibility'}}</mat-icon>
              </button>
              <mat-error *ngIf="resetForm.get('newPassword')?.hasError('required')">
                Nova senha é obrigatória
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="fill">
              <mat-label>Confirmar Nova Senha</mat-label>
              <input matInput [type]="hideConfirmPassword ? 'password' : 'text'" formControlName="confirmPassword" required>
              <button mat-icon-button matSuffix (click)="hideConfirmPassword = !hideConfirmPassword" type="button">
                <mat-icon>{{hideConfirmPassword ? 'visibility_off' : 'visibility'}}</mat-icon>
              </button>
              <mat-error *ngIf="resetForm.get('confirmPassword')?.hasError('required')">
                Confirmação de senha é obrigatória
              </mat-error>
              <mat-error *ngIf="resetForm.hasError('passwordMismatch')">
                As senhas não coincidem
              </mat-error>
            </mat-form-field>

            <div class="button-container">
              <button mat-button type="button" (click)="goBack()">Voltar</button>
              <button mat-raised-button color="primary" type="submit" [disabled]="resetForm.invalid">
                Redefinir Senha
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
    .reset-password-card {
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
export class ResetPasswordComponent {
  resetForm: FormGroup;
  hideNewPassword = true;
  hideConfirmPassword = true;

  constructor(
    private fb: FormBuilder,
    private passwordResetService: PasswordResetService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.resetForm = this.fb.group({
      token: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('newPassword')?.value === g.get('confirmPassword')?.value
      ? null
      : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.resetForm.valid) {
      const { token, newPassword, confirmPassword } = this.resetForm.value;
      this.passwordResetService.resetPassword(token, newPassword, confirmPassword).subscribe({
        next: (response) => {
          this.snackBar.open(response, 'OK', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          }).afterDismissed().subscribe(() => {
            this.router.navigate(['/login']);
          });
        },
        error: (error) => {
          this.snackBar.open('Erro ao redefinir senha. Verifique o token e tente novamente.', 'OK', {
            duration: 5000
          });
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['/request-password-reset']);
  }
} 