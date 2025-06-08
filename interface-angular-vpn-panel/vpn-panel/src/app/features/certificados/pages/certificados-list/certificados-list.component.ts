import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-certificados-list',
  template: `
    <div class="container">
      <h1>Certificados</h1>
      <p>Lista de certificados será implementada aqui.</p>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
    }
  `]
})
export class CertificadosListComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
} 