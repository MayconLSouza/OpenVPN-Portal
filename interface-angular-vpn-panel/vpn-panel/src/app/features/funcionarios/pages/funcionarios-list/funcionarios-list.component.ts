import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-funcionarios-list',
  template: `
    <div class="container">
      <h1>Funcionários</h1>
      <p>Lista de funcionários será implementada aqui.</p>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
    }
  `]
})
export class FuncionariosListComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
} 