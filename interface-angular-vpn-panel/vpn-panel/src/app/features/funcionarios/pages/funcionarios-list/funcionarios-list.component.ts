import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FuncionarioService, Funcionario } from '../../../../core/services/funcionario.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-funcionarios-list',
  templateUrl: './funcionarios-list.component.html',
  styleUrls: ['./funcionarios-list.component.scss']
})
export class FuncionariosListComponent implements OnInit {
  displayedColumns: string[] = ['nome', 'email', 'usuario', 'admin', 'status', 'acoes'];
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

    this.funcionarioService.listar().subscribe({
      next: (funcionarios) => {
        this.dataSource.data = funcionarios;
        this.nenhumResultado = funcionarios.length === 0;
        this.carregando = false;
      },
      error: (error) => {
        console.error('Erro ao carregar funcionários:', error);
        this.erro = true;
        this.snackBar.open(error.message || 'Erro ao carregar funcionários', 'OK', { duration: 3000 });
        this.carregando = false;
      }
    });
  }

  aplicarFiltro() {
    this.carregando = true;
    this.nenhumResultado = false;
    this.erro = false;

    this.funcionarioService.filtrar(this.termoFiltro).subscribe({
      next: (funcionarios) => {
        this.dataSource.data = funcionarios;
        this.nenhumResultado = funcionarios.length === 0;
        this.carregando = false;
      },
      error: (error) => {
        console.error('Erro ao filtrar funcionários:', error);
        this.erro = true;
        this.snackBar.open(error.message || 'Erro ao filtrar funcionários', 'OK', { duration: 3000 });
        this.carregando = false;
      }
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
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar Eleição',
        message: `Deseja realmente eleger ${funcionario.nome} como administrador?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.funcionarioService.elegerAdministrador(funcionario.idFuncionario).subscribe({
          next: () => {
            this.snackBar.open('Funcionário eleito como administrador com sucesso!', 'OK', { duration: 3000 });
            this.carregarFuncionarios();
          },
          error: (error) => {
            console.error('Erro ao eleger administrador:', error);
            this.snackBar.open(error.message || 'Erro ao eleger administrador', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }

  revogarAdmin(funcionario: Funcionario) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar Revogação',
        message: `Deseja realmente revogar a permissão de administrador de ${funcionario.nome}?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.funcionarioService.revogarAdministrador(funcionario.idFuncionario).subscribe({
          next: () => {
            this.snackBar.open('Permissão de administrador revogada com sucesso!', 'OK', { duration: 3000 });
            this.carregarFuncionarios();
          },
          error: (error) => {
            console.error('Erro ao revogar administrador:', error);
            this.snackBar.open(error.message || 'Erro ao revogar permissão de administrador', 'OK', { duration: 3000 });
          }
        });
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
          ? this.funcionarioService.reativarAcesso(funcionario.idFuncionario)
          : this.funcionarioService.revogarAcesso(funcionario.idFuncionario);

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
          error: (error) => {
            console.error('Erro ao gerenciar acesso:', error);
            this.snackBar.open(
              error.message || (ativar
                ? 'Erro ao reativar acesso do funcionário'
                : 'Erro ao revogar acesso do funcionário'),
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
        this.funcionarioService.removerFuncionario(funcionario.idFuncionario).subscribe({
          next: () => {
            this.snackBar.open('Funcionário removido com sucesso!', 'OK', { duration: 3000 });
            this.carregarFuncionarios();
          },
          error: (error) => {
            console.error('Erro ao remover funcionário:', error);
            this.snackBar.open(error.message || 'Erro ao remover funcionário', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }
} 