import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  template: `
    <div class="dashboard-container">
      <h1>Dashboard</h1>
      
      <div class="dashboard-grid">
        <mat-card>
          <mat-card-header>
            <mat-icon mat-card-avatar>vpn_key</mat-icon>
            <mat-card-title>Certificados</mat-card-title>
            <mat-card-subtitle>Status dos certificados</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>Total de certificados: XX</p>
            <p>Certificados a vencer: XX</p>
          </mat-card-content>
          <mat-card-actions>
            <button mat-button color="primary" routerLink="/certificados">Ver detalhes</button>
          </mat-card-actions>
        </mat-card>

        <mat-card *ngIf="isAdmin$ | async">
          <mat-card-header>
            <mat-icon mat-card-avatar>people</mat-icon>
            <mat-card-title>Funcionários</mat-card-title>
            <mat-card-subtitle>Status dos funcionários</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>Total de funcionários: XX</p>
            <p>Funcionários ativos: XX</p>
          </mat-card-content>
          <mat-card-actions>
            <button mat-button color="primary" routerLink="/funcionarios">Ver detalhes</button>
          </mat-card-actions>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      padding: 20px;
    }

    .dashboard-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
      margin-top: 20px;
    }

    mat-card {
      height: 100%;
    }

    mat-card-header {
      margin-bottom: 16px;
    }

    mat-icon[mat-card-avatar] {
      width: 40px;
      height: 40px;
      font-size: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  `]
})
export class DashboardComponent implements OnInit {
  isAdmin$ = this.authService.currentUser$.pipe(
    map(user => user?.isAdmin ?? false)
  );

  constructor(private authService: AuthService) {}

  ngOnInit(): void {}
} 