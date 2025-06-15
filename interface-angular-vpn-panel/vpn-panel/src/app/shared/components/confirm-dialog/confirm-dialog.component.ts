import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface ConfirmDialogData {
  title: string;
  message: string;
  actionType: 'delete' | 'revoke' | 'save' | 'promote' | 'demote';
}

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}

  getActionColor(): string {
    switch (this.data.actionType) {
      case 'delete':
      case 'revoke':
        return 'mat-warn';
      case 'promote':
      case 'save':
        return 'mat-primary';
      default:
        return 'mat-primary';
    }
  }

  getConfirmText(): string {
    switch (this.data.actionType) {
      case 'delete':
        return 'Remover';
      case 'revoke':
        return 'Revogar';
      case 'save':
        return 'Salvar';
      case 'promote':
        return 'Promover';
      case 'demote':
        return 'Rebaixar';
      default:
        return 'Confirmar';
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onConfirm(): void {
    this.dialogRef.close(true);
  }
} 