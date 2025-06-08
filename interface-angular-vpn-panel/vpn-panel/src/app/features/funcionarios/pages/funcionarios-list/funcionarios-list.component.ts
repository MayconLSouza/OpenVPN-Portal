import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FuncionarioService, Funcionario } from '../../../../core/services/funcionario.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-funcionarios-list',
  template: `
    <div class="container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Gerenciamento de Funcionários</mat-card-title>
          <mat-card-subtitle>Lista de funcionários cadastrados no sistema</mat-card-subtitle>
        </mat-card-header>

        <mat-card-content>
          <div class="actions">
            <button mat-raised-button color="primary" (click)="cadastrarFuncionario()">
              <mat-icon>add</mat-icon>
              Novo Funcionário
            </button>
          </div>

          <table mat-table [dataSource]="funcionarios" class="mat-elevation-z8">
            <ng-container matColumnDef="nome">
              <th mat-header-cell *matHeaderCellDef>Nome</th>
              <td mat-cell *matCellDef="let funcionario">{{funcionario.nome}}</td>
            </ng-container>

            <ng-container matColumnDef="email">
              <th mat-header-cell *matHeaderCellDef>Email</th>
              <td mat-cell *matCellDef="let funcionario">{{funcionario.email}}</td>
            </ng-container>

            <ng-container matColumnDef="cargo">
              <th mat-header-cell *matHeaderCellDef>Cargo</th>
              <td mat-cell *matCellDef="let funcionario">{{funcionario.cargo}}</td>
            </ng-container>

            <ng-container matColumnDef="admin">
              <th mat-header-cell *matHeaderCellDef>Admin</th>
              <td mat-cell *matCellDef="let funcionario">
                <mat-icon color="{{funcionario.admin ? 'primary' : ''}}">
                  {{funcionario.admin ? 'check_circle' : 'cancel'}}
                </mat-icon>
              </td>
            </ng-container>

            <ng-container matColumnDef="ativo">
              <th mat-header-cell *matHeaderCellDef>Status</th>
              <td mat-cell *matCellDef="let funcionario">
                <span [class]="funcionario.ativo ? 'status-ativo' : 'status-inativo'">
                  {{funcionario.ativo ? 'Ativo' : 'Inativo'}}
                </span>
              </td>
            </ng-container>

            <ng-container matColumnDef="acoes">
              <th mat-header-cell *matHeaderCellDef>Ações</th>
              <td mat-cell *matCellDef="let funcionario">
                <button mat-icon-button [matMenuTriggerFor]="menu" aria-label="Ações">
                  <mat-icon>more_vert</mat-icon>
                </button>
                <mat-menu #menu="matMenu">
                  <button mat-menu-item *ngIf="!funcionario.admin" (click)="elegerAdmin(funcionario)">
                    <mat-icon>admin_panel_settings</mat-icon>
                    <span>Eleger Administrador</span>
                  </button>
                  <button mat-menu-item *ngIf="funcionario.admin" (click)="revogarAdmin(funcionario)">
                    <mat-icon>person_off</mat-icon>
                    <span>Revogar Admin</span>
                  </button>
                  <button mat-menu-item *ngIf="funcionario.ativo" (click)="gerenciarAcesso(funcionario, false)">
                    <mat-icon>no_accounts</mat-icon>
                    <span>Revogar Acesso</span>
                  </button>
                  <button mat-menu-item *ngIf="!funcionario.ativo" (click)="gerenciarAcesso(funcionario, true)">
                    <mat-icon>how_to_reg</mat-icon>
                    <span>Reativar Acesso</span>
                  </button>
                  <button mat-menu-item (click)="remover(funcionario)">
                    <mat-icon>delete</mat-icon>
                    <span>Remover</span>
                  </button>
                </mat-menu>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
          </table>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
    }
    .actions {
      margin-bottom: 20px;
    }
    table {
      width: 100%;
    }
    .mat-column-acoes {
      width: 80px;
      text-align: center;
    }
    .mat-column-admin, .mat-column-ativo {
      width: 100px;
      text-align: center;
    }
    .status-ativo {
      color: #4CAF50;
      font-weight: 500;
    }
    .status-inativo {
      color: #F44336;
      font-weight: 500;
    }
  `]
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
        title: 'Confirmar Exclusão',
        message: `Deseja realmente remover o funcionário ${funcionario.nome}?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.funcionarioService.remover(funcionario.id).subscribe({
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