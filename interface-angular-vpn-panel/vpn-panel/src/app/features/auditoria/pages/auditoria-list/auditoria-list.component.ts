import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-auditoria-list',
  template: `
    <div class="container">
      <h1>Log de Auditoria</h1>
      <p>Lista de logs será implementada aqui.</p>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
    }
  `]
})
export class AuditoriaListComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
} 