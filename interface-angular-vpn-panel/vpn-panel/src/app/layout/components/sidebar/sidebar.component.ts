import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-sidebar',
  template: `
    <mat-nav-list>
      <a mat-list-item routerLink="/dashboard" routerLinkActive="active">
        <mat-icon matListItemIcon>dashboard</mat-icon>
        <span matListItemTitle>Dashboard</span>
      </a>

      <a mat-list-item routerLink="/certificados" routerLinkActive="active">
        <mat-icon matListItemIcon>vpn_key</mat-icon>
        <span matListItemTitle>Certificados</span>
      </a>

      <ng-container *ngIf="isAdmin$ | async">
        <mat-divider></mat-divider>
        
        <a mat-list-item routerLink="/funcionarios" routerLinkActive="active">
          <mat-icon matListItemIcon>people</mat-icon>
          <span matListItemTitle>Funcionários</span>
        </a>

        <a mat-list-item routerLink="/auditoria" routerLinkActive="active">
          <mat-icon matListItemIcon>history</mat-icon>
          <span matListItemTitle>Log de Auditoria</span>
        </a>
      </ng-container>
    </mat-nav-list>
  `,
  styles: [`
    .active {
      background-color: rgba(0, 0, 0, 0.04);
    }
  `]
})
export class SidebarComponent implements OnInit {
  isAdmin$ = this.authService.currentUser$.pipe(
    map(user => user?.isAdmin ?? false)
  );

  constructor(private authService: AuthService) {}

  ngOnInit(): void {}
} 