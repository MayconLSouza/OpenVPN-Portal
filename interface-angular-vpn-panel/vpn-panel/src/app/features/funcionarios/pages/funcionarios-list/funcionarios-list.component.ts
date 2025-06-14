import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FuncionarioService, Funcionario } from '../../../../core/services/funcionario.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-funcionarios-list',
  templateUrl: './funcionarios-list.component.html',
  styleUrls: ['./funcionarios-list.component.scss']
})
export class FuncionariosListComponent implements OnInit {
  funcionarios: Funcionario[] = [];
  displayedColumns: string[] = ['nome', 'email', 'cargo', 'admin', 'ativo', 'acoes'];

  constructor(
    private funcionarioService: FuncionarioService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.carregarFuncionarios();
  }

  carregarFuncionarios() {
    this.funcionarioService.listar().subscribe({
      next: (funcionarios) => {
        this.funcionarios = funcionarios;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar funcionários', 'OK', { duration: 3000 });
      }
    });
  }

  cadastrarFuncionario(): void {
    this.router.navigate(['/funcionarios/cadastro']);
  }

  elegerAdmin(funcionario: Funcionario) {
    this.funcionarioService.elegerAdministrador(funcionario.id).subscribe({
      next: () => {
        this.snackBar.open('Funcionário eleito como administrador com sucesso!', 'OK', { duration: 3000 });
        this.carregarFuncionarios();
      },
      error: () => {
        this.snackBar.open('Erro ao eleger administrador', 'OK', { duration: 3000 });
      }
    });
  }

  revogarAdmin(funcionario: Funcionario) {
    this.funcionarioService.revogarAdministrador(funcionario.id).subscribe({
      next: () => {
        this.snackBar.open('Permissão de administrador revogada com sucesso!', 'OK', { duration: 3000 });
        this.carregarFuncionarios();
      },
      error: () => {
        this.snackBar.open('Erro ao revogar permissão de administrador', 'OK', { duration: 3000 });
      }
    });
  }

  gerenciarAcesso(funcionario: Funcionario, ativar: boolean) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: ativar ? 'Confirmar Reativação' : 'Confirmar Revogação',
        message: ativar 
          ? `Deseja realmente reativar o acesso de ${funcionario.nome}?`
          : `Deseja realmente revogar o acesso de ${funcionario.nome}?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const action = ativar 
          ? this.funcionarioService.reativarAcesso(funcionario.id)
          : this.funcionarioService.revogarAcesso(funcionario.id);

        action.subscribe({
          next: () => {
            this.snackBar.open(
              ativar 
                ? 'Acesso do funcionário reativado com sucesso!'
                : 'Acesso do funcionário revogado com sucesso!',
              'OK',
              { duration: 3000 }
            );
            this.carregarFuncionarios();
          },
          error: () => {
            this.snackBar.open(
              ativar
                ? 'Erro ao reativar acesso do funcionário'
                : 'Erro ao revogar acesso do funcionário',
              'OK',
              { duration: 3000 }
            );
          }
        });
      }
    });
  }

  remover(funcionario: Funcionario) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar Remoção',
        message: `Deseja realmente remover o funcionário ${funcionario.nome}?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.funcionarioService.removerFuncionario(funcionario.id).subscribe({
          next: () => {
            this.snackBar.open('Funcionário removido com sucesso!', 'OK', { duration: 3000 });
            this.carregarFuncionarios();
          },
          error: () => {
            this.snackBar.open('Erro ao remover funcionário', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }
} 