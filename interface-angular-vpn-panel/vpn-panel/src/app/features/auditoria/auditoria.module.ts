import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../shared/material.module';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule.forChild([
      {
        path: '',
        loadChildren: () => import('./pages/auditoria-list/auditoria-list.module').then(m => m.AuditoriaListModule)
      }
    ])
  ]
})
export class AuditoriaModule { } 