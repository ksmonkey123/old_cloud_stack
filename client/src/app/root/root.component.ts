import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { UserService } from '../user/user.service';
import { User } from '../model/user';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit {

  isCollapsed = true;

  links = [
    { text: "Shortener", route: "/shorten" }
  ]

  user: User;

  username: string;
  alerts: Alert[] = [];


  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router) { }

  ngOnInit() {
    this.userService.getUserInfo().subscribe(
      (u: User) => {
        this.user = u;
        if (u.admin)
          this.username = u.username + " (Admin)";
        else
          this.username = u.username + " (User)";
      },
      (error: Error) => {
        console.log(error)
      })
    console.log("hi");
  }

  doLogout() {
    this.authService.fullLogout(() =>
      this.router.navigate(['/login'])
    );
  }

  openSettings() {
    this.router.navigate(['/user']);
  }

  addAlert(alert: Alert) {
    this.alerts.push(alert);
  }

  closeAlerts(parent: any) {
    this.alerts = this.alerts.filter((x: Alert) => x.parent !== parent);
  }

  closeAlert(alert: Alert) {
    const index: number = this.alerts.indexOf(alert);
    this.alerts.splice(index, 1);
  }

  addErrorAlert(alert: HttpErrorResponse, parent: any) {
    let status = alert.status;
    let message: string = "";

    switch (status) {
      case 400: {
        message = "Could not perform operation: Invalid Input";
        break;
      }
      default: {
        message = "Operation failed: " + status + " - " + alert.statusText + " : " + alert.error.message;
      }
    }

    this.addAlert({ type: 'danger', message: message, parent: parent });

  }

}

export interface Alert {
  type: string;
  message: string;
  parent: any;
}