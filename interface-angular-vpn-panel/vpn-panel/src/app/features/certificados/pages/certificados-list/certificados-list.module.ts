import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../../shared/material.module';
import { CertificadosListComponent } from './certificados-list.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [CertificadosListComponent],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    RouterModule.forChild([
      { path: '', component: CertificadosListComponent }
    ])
  ]
})
export class CertificadosListModule { } 