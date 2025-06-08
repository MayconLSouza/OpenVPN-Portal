import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { AuditoriaListComponent } from './auditoria-list.component';

@NgModule({
  declarations: [AuditoriaListComponent],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule.forChild([
      { path: '', component: AuditoriaListComponent }
    ])
  ]
})
export class AuditoriaListModule { } 