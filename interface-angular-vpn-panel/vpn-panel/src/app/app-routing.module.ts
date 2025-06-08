import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './layout/components/main-layout/main-layout.component';
import { LoginComponent } from './auth/components/login/login.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'certificados',
        loadChildren: () => import('./features/certificados/certificados.module').then(m => m.CertificadosModule)
      },
      {
        path: 'funcionarios',
        loadChildren: () => import('./features/funcionarios/funcionarios.module').then(m => m.FuncionariosModule),
        data: { requiresAdmin: true }
      },
      {
        path: 'auditoria',
        loadChildren: () => import('./features/auditoria/auditoria.module').then(m => m.AuditoriaModule),
        data: { requiresAdmin: true }
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { } 