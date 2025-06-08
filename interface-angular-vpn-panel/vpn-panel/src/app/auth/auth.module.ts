import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../shared/material.module';

import { LoginComponent } from './components/login/login.component';
import { RequestPasswordResetComponent } from './request-password-reset/request-password-reset.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';

@NgModule({
  declarations: [
    LoginComponent,
    RequestPasswordResetComponent,
    ResetPasswordComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MaterialModule
  ],
  exports: [
    LoginComponent,
    RequestPasswordResetComponent,
    ResetPasswordComponent
  ]
})
export class AuthModule { } 