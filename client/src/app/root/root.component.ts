import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { UserService } from '../user/user.service';
import { User } from '../model/user';
import { HttpErrorResponse } from '@angular/common/http';
import { Alert } from './alert';

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit {

  isCollapsed = true;

  links :Link[] = [
    new Link("Shortener", "/shorten", "ROLE_SHORTEN", "a simple URL-shortener implementation"),
    new Link("E:D Router", "/elite-router", "ROLE_ELITE", "an exhaustive pathfinder for Elite:Dangerous"),
    new Link("Netcode", "/netcode", "ROLE_NETCODE", "app registry for the my.awae.ch netcode server"),
    new Link("Youtube-Download", "/ytdl/list", "ROLE_YTDL", "a youtube video downloader and transcoder")
  ];

  user: User;

  isAdmin: boolean = false;
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
        if (u.roles.includes("ROLE_ADMIN")) {
          this.username = u.username + " (Admin)";
          this.isAdmin = true;
        }
        else
          this.username = u.username + " (User)";
        this.links = this.links.filter(link => u.roles.includes(link.role));
      },
      (error: Error) => {
        console.log(error)
      });
  }

  doLogout() {
    this.authService.fullLogout(() =>
      this.router.navigate(['/login'])
    );
  }

  openSettings() {
    this.router.navigate(['/user']);
  }

  openAdminPanel() {
    this.router.navigate(['/admin']);
  }

  addAlert(alert: Alert) {
    this.alerts.push(alert);
  }

  addSuccessAlert(message: string, parent: any) {
    this.addAlert({ type: "success", message: message, parent: parent });
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

export class Link {

  constructor(
    public text: string,
    public route: string,
    public role: string,
    public desc: string
  ) {}

}