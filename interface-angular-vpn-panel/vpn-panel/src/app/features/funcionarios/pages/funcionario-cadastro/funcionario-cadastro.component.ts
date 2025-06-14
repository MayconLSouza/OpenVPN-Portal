import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FuncionarioService, FuncionarioCreate } from '../../../../core/services/funcionario.service';

@Component({
  selector: 'app-funcionario-cadastro',
  templateUrl: './funcionario-cadastro.component.html',
  styleUrls: ['./funcionario-cadastro.component.scss']
})
export class FuncionarioCadastroComponent implements OnInit {
  form: FormGroup;
  hideSenha = true;
  hideConfirmSenha = true;

  constructor(
    private fb: FormBuilder,
    private funcionarioService: FuncionarioService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      usuario: ['', [Validators.required, Validators.minLength(3)]],
      cargo: ['', Validators.required],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required],
      admin: [false]
    }, {
      validators: this.senhasIguais
    });
  }

  ngOnInit(): void {
  }

  senhasIguais(group: FormGroup) {
    const senha = group.get('senha')?.value;
    const confirmarSenha = group.get('confirmarSenha')?.value;

    if (senha === confirmarSenha) {
      return null;
    }

    return { senhasDiferentes: true };
  }

  onSubmit() {
    if (this.form.valid) {
      const funcionario: FuncionarioCreate = {
        nome: this.form.get('nome')?.value,
        email: this.form.get('email')?.value,
        usuario: this.form.get('usuario')?.value,
        cargo: this.form.get('cargo')?.value,
        senha: this.form.get('senha')?.value,
        admin: this.form.get('admin')?.value
      };

      this.funcionarioService.cadastrarFuncionario(funcionario).subscribe({
        next: () => {
          this.snackBar.open('Funcionário cadastrado com sucesso!', 'OK', { duration: 3000 });
          this.router.navigate(['/funcionarios']);
        },
        error: () => {
          this.snackBar.open('Erro ao cadastrar funcionário', 'OK', { duration: 3000 });
        }
      });
    }
  }

  voltar() {
    this.router.navigate(['/funcionarios']);
  }
} 