import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { UserService } from '../shared/user.service';
import { User } from '../model/user';

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit {

  isCollapsed = true;

  username : string;

  links = [
    { text: "Shortener", route: "/shorten" }
  ]

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router) { }

  ngOnInit() {
    this.userService.getUserInfo().subscribe(
      (u: User) => {
        if (u.admin)
        this.username = u.username + " (Admin)";
        else
        this.username = u.username + " (User)";
      },
      (error: Error) => {
        console.log(error)
      })
  }

  doLogout() {
    this.authService.fullLogout(() =>
      this.router.navigate(['/login'])
    );
  }

  openSettings() {
    this.router.navigate(['/user']);
  }

}
