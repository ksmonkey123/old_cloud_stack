import { Component, OnInit } from '@angular/core';
import { UserService } from '../shared/user.service';
import { User } from '../model/user';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router) { }

  user: any = {};

  ngOnInit() {
    this.userService.getUserInfo().subscribe(
      (data: User) => this.user = data
    );
  }

  doLogout() {
    this.authService.fullLogout(() =>
      this.router.navigate(['/login'])
    );
  }

}
