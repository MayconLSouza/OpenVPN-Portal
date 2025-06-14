import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordResetService } from '../../core/services/password-reset.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-request-password-reset',
  templateUrl: './request-password-reset.component.html',
  styleUrls: ['./request-password-reset.component.scss']
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