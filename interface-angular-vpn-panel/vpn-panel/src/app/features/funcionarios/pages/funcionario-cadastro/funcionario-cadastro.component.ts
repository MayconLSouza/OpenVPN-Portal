import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FuncionarioService, FuncionarioCreate } from '../../../../core/services/funcionario.service';

@Component({
  selector: 'app-funcionario-cadastro',
  template: `
    <div class="container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Cadastro de Funcionário</mat-card-title>
          <mat-card-subtitle>Preencha os dados do novo funcionário</mat-card-subtitle>
        </mat-card-header>

        <form [formGroup]="funcionarioForm" (ngSubmit)="onSubmit()">
          <mat-card-content>
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Nome</mat-label>
              <input matInput formControlName="nome" required>
              <mat-error *ngIf="funcionarioForm.get('nome')?.hasError('required')">
                Nome é obrigatório
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Email</mat-label>
              <input matInput formControlName="email" required type="email">
              <mat-error *ngIf="funcionarioForm.get('email')?.hasError('required')">
                Email é obrigatório
              </mat-error>
              <mat-error *ngIf="funcionarioForm.get('email')?.hasError('email')">
                Email inválido
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Usuário</mat-label>
              <input matInput formControlName="usuario" required>
              <mat-error *ngIf="funcionarioForm.get('usuario')?.hasError('required')">
                Usuário é obrigatório
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Cargo</mat-label>
              <input matInput formControlName="cargo" required>
              <mat-error *ngIf="funcionarioForm.get('cargo')?.hasError('required')">
                Cargo é obrigatório
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Senha</mat-label>
              <input matInput [type]="hideSenha ? 'password' : 'text'" formControlName="senha" required>
              <button mat-icon-button matSuffix (click)="hideSenha = !hideSenha" type="button">
                <mat-icon>{{hideSenha ? 'visibility_off' : 'visibility'}}</mat-icon>
              </button>
              <mat-error *ngIf="funcionarioForm.get('senha')?.hasError('required')">
                Senha é obrigatória
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Confirmar Senha</mat-label>
              <input matInput [type]="hideConfirmSenha ? 'password' : 'text'" formControlName="confirmSenha" required>
              <button mat-icon-button matSuffix (click)="hideConfirmSenha = !hideConfirmSenha" type="button">
                <mat-icon>{{hideConfirmSenha ? 'visibility_off' : 'visibility'}}</mat-icon>
              </button>
              <mat-error *ngIf="funcionarioForm.get('confirmSenha')?.hasError('required')">
                Confirmação de senha é obrigatória
              </mat-error>
              <mat-error *ngIf="funcionarioForm.hasError('senhasDiferentes')">
                As senhas não coincidem
              </mat-error>
            </mat-form-field>

            <mat-checkbox formControlName="admin" class="admin-checkbox">
              Usuário Administrador
            </mat-checkbox>
          </mat-card-content>

          <mat-card-actions align="end">
            <button mat-button type="button" (click)="voltar()">Cancelar</button>
            <button mat-raised-button color="primary" type="submit" [disabled]="funcionarioForm.invalid || loading">
              <mat-icon *ngIf="!loading">save</mat-icon>
              <mat-spinner diameter="20" *ngIf="loading"></mat-spinner>
              Salvar
            </button>
          </mat-card-actions>
        </form>
      </mat-card>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
    }
    .full-width {
      width: 100%;
      margin-bottom: 16px;
    }
    .admin-checkbox {
      margin-bottom: 16px;
    }
    mat-spinner {
      display: inline-block;
      margin-right: 8px;
    }
  `]
})
export class FuncionarioCadastroComponent {
  funcionarioForm: FormGroup;
  loading = false;
  hideSenha = true;
  hideConfirmSenha = true;

  constructor(
    private fb: FormBuilder,
    private funcionarioService: FuncionarioService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.funcionarioForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      usuario: ['', Validators.required],
      cargo: ['', Validators.required],
      senha: ['', Validators.required],
      confirmSenha: ['', Validators.required],
      admin: [false]
    }, { validator: this.senhasIguaisValidator });
  }

  senhasIguaisValidator(g: FormGroup) {
    return g.get('senha')?.value === g.get('confirmSenha')?.value
      ? null
      : { senhasDiferentes: true };
  }

  onSubmit() {
    if (this.funcionarioForm.valid && !this.loading) {
      this.loading = true;
      const formData = this.funcionarioForm.value;
      
      const funcionario: FuncionarioCreate = {
        nome: formData.nome,
        email: formData.email,
        usuario: formData.usuario,
        senha: formData.senha,
        cargo: formData.cargo,
        admin: formData.admin
      };

      this.funcionarioService.cadastrarFuncionario(funcionario).subscribe({
        next: () => {
          this.snackBar.open('Funcionário cadastrado com sucesso!', 'OK', { duration: 3000 });
          this.router.navigate(['/funcionarios']);
        },
        error: (error) => {
          this.snackBar.open(
            error.error?.message || 'Erro ao cadastrar funcionário. Tente novamente.',
            'OK',
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }

  voltar() {
    this.router.navigate(['/funcionarios']);
  }
} 