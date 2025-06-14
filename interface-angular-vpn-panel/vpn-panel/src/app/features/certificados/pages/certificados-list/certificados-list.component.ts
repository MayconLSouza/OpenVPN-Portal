import { Component, OnInit } from '@angular/core';
import { CertificadoService } from 'src/app/core/services/certificado.service';
import { Certificado } from 'src/app/core/models/certificado.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-certificados-list',
  templateUrl: './certificados-list.component.html',
  styleUrls: ['./certificados-list.component.scss']
})
export class CertificadosListComponent implements OnInit {
  certificados: Certificado[] = [];
  certificadosFiltrados: Certificado[] = [];
  selecionados: Set<string> = new Set();
  busca: string = '';
  filtroData: string = '';
  carregando: boolean = false;
  criando: boolean = false;

  constructor(
    private certificadoService: CertificadoService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.carregarCertificados();
  }

  carregarCertificados(): void {
    this.carregando = true;
    this.certificadoService.listar().subscribe({
      next: (certs) => {
        this.certificados = certs;
        this.aplicarFiltro();
        this.carregando = false;
      },
      error: () => { this.carregando = false; }
    });
  }

  aplicarFiltro(): void {
    this.certificadosFiltrados = this.certificados.filter(cert => {
      const buscaOk = !this.busca || cert.id.toLowerCase().includes(this.busca.toLowerCase());
      const dataOk = !this.filtroData || (new Date(cert.dataCriacao).toISOString().slice(0,10) === this.filtroData);
      return buscaOk && dataOk;
    });
  }

  onBuscaChange(): void {
    this.aplicarFiltro();
  }

  onDataChange(): void {
    this.aplicarFiltro();
  }

  toggleSelecionado(id: string): void {
    if (this.selecionados.has(id)) {
      this.selecionados.delete(id);
    } else {
      this.selecionados.add(id);
    }
  }

  selecionarTodos(): void {
    if (this.todosSelecionados()) {
      this.selecionados.clear();
    } else {
      this.certificadosFiltrados.forEach(cert => this.selecionados.add(cert.id));
    }
  }

  todosSelecionados(): boolean {
    return this.certificadosFiltrados.length > 0 && this.certificadosFiltrados.every(cert => this.selecionados.has(cert.id));
  }

  criarCertificado(): void {
    this.criando = true;
    this.certificadoService.criar().subscribe({
      next: () => {
        this.criando = false;
        this.carregarCertificados();
      },
      error: () => { this.criando = false; }
    });
  }

  removerSelecionados(): void {
    const ids = Array.from(this.selecionados);
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar remoção',
        message: `Você realmente deseja excluir os arquivos ${ids.join(' e ')}?`
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.certificadoService.remover(ids).subscribe(() => {
          this.selecionados.clear();
          this.carregarCertificados();
        });
      }
    });
  }

  baixarCertificado(id: string): void {
    this.certificadoService.baixar(id).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${id}.zip`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
} 