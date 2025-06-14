import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FuncionarioService, Funcionario } from '../../../../core/services/funcionario.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-funcionarios-list',
  templateUrl: './funcionarios-list.component.html',
  styleUrls: ['./funcionarios-list.component.scss']
})
export class FuncionariosListComponent implements OnInit {
  displayedColumns: string[] = ['nome', 'email', 'cargo', 'admin', 'ativo', 'acoes'];
  dataSource: MatTableDataSource<Funcionario>;
  termoFiltro: string = '';
  carregando: boolean = false;
  nenhumResultado: boolean = false;
  erro: boolean = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private funcionarioService: FuncionarioService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.dataSource = new MatTableDataSource<Funcionario>([]);
  }

  ngOnInit(): void {
    this.carregarFuncionarios();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  carregarFuncionarios() {
    this.carregando = true;
    this.nenhumResultado = false;
    this.erro = false;

    this.funcionarioService.listar()
      .pipe(
        catchError(error => {
          console.error('Erro ao carregar funcionários:', error);
          this.erro = true;
          this.snackBar.open('Erro ao carregar funcionários', 'OK', { duration: 3000 });
          return of([]);
        }),
        finalize(() => {
          this.carregando = false;
        })
      )
      .subscribe(funcionarios => {
        this.dataSource.data = funcionarios;
        this.nenhumResultado = funcionarios.length === 0;
      });
  }

  aplicarFiltro() {
    this.carregando = true;
    this.nenhumResultado = false;
    this.erro = false;

    this.funcionarioService.filtrar(this.termoFiltro)
      .pipe(
        catchError(error => {
          console.error('Erro ao filtrar funcionários:', error);
          this.erro = true;
          this.snackBar.open('Erro ao filtrar funcionários', 'OK', { duration: 3000 });
          return of([]);
        }),
        finalize(() => {
          this.carregando = false;
        })
      )
      .subscribe(funcionarios => {
        this.dataSource.data = funcionarios;
        this.nenhumResultado = funcionarios.length === 0;
      });
  }

  limparFiltro() {
    this.termoFiltro = '';
    this.carregarFuncionarios();
  }

  cadastrarFuncionario(): void {
    this.router.navigate(['/funcionarios/cadastro']);
  }

  elegerAdmin(funcionario: Funcionario) {
    this.funcionarioService.elegerAdministrador(funcionario.id)
      .pipe(
        catchError(error => {
          console.error('Erro ao eleger administrador:', error);
          this.snackBar.open('Erro ao eleger administrador', 'OK', { duration: 3000 });
          return of(null);
        })
      )
      .subscribe(result => {
        if (result) {
          this.snackBar.open('Funcionário eleito como administrador com sucesso!', 'OK', { duration: 3000 });
          this.carregarFuncionarios();
        }
      });
  }

  revogarAdmin(funcionario: Funcionario) {
    this.funcionarioService.revogarAdministrador(funcionario.id)
      .pipe(
        catchError(error => {
          console.error('Erro ao revogar administrador:', error);
          this.snackBar.open('Erro ao revogar permissão de administrador', 'OK', { duration: 3000 });
          return of(null);
        })
      )
      .subscribe(result => {
        if (result) {
          this.snackBar.open('Permissão de administrador revogada com sucesso!', 'OK', { duration: 3000 });
          this.carregarFuncionarios();
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

        action
          .pipe(
            catchError(error => {
              console.error('Erro ao gerenciar acesso:', error);
              this.snackBar.open(
                ativar
                  ? 'Erro ao reativar acesso do funcionário'
                  : 'Erro ao revogar acesso do funcionário',
                'OK',
                { duration: 3000 }
              );
              return of(null);
            })
          )
          .subscribe(result => {
            if (result) {
              this.snackBar.open(
                ativar 
                  ? 'Acesso do funcionário reativado com sucesso!'
                  : 'Acesso do funcionário revogado com sucesso!',
                'OK',
                { duration: 3000 }
              );
              this.carregarFuncionarios();
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
        this.funcionarioService.removerFuncionario(funcionario.id)
          .pipe(
            catchError(error => {
              console.error('Erro ao remover funcionário:', error);
              this.snackBar.open('Erro ao remover funcionário', 'OK', { duration: 3000 });
              return of(null);
            })
          )
          .subscribe(result => {
            if (result !== null) {
              this.snackBar.open('Funcionário removido com sucesso!', 'OK', { duration: 3000 });
              this.carregarFuncionarios();
            }
          });
      }
    });
  }
} 