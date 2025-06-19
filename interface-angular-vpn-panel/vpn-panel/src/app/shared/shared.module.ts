import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    ConfirmDialogComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule
  ],
  exports: [
    CommonModule,
    MaterialModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    ConfirmDialogComponent
  ]
})
export class SharedModule { } 