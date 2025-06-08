import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { FuncionariosListComponent } from './funcionarios-list.component';

@NgModule({
  declarations: [FuncionariosListComponent],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule.forChild([
      { path: '', component: FuncionariosListComponent }
    ])
  ]
})
export class FuncionariosListModule { } 