import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  isAdmin$ = this.authService.currentUser$.pipe(
    map(user => user?.isAdmin ?? false)
  );

  constructor(private authService: AuthService) {}

  ngOnInit(): void {}
} 