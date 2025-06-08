import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { MaterialModule } from '../../shared/material.module';
import { FuncionariosListComponent } from './pages/funcionarios-list/funcionarios-list.component';
import { FuncionarioCadastroComponent } from './pages/funcionario-cadastro/funcionario-cadastro.component';

@NgModule({
  declarations: [
    FuncionariosListComponent,
    FuncionarioCadastroComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      { path: '', component: FuncionariosListComponent },
      { path: 'cadastro', component: FuncionarioCadastroComponent }
    ]),
    ReactiveFormsModule,
    SharedModule,
    MaterialModule
  ]
})
export class FuncionariosModule { } 