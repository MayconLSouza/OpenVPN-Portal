import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [
    ConfirmDialogComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    MatDialogModule,
    MatButtonModule
  ],
  exports: [
    ConfirmDialogComponent,
    MaterialModule,
    MatDialogModule
  ]
})
export class SharedModule { } 