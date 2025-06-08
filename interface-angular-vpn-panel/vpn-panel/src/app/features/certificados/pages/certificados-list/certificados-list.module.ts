import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { CertificadosListComponent } from './certificados-list.component';

@NgModule({
  declarations: [CertificadosListComponent],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule.forChild([
      { path: '', component: CertificadosListComponent }
    ])
  ]
})
export class CertificadosListModule { } 