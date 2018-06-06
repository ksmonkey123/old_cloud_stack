import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RootComponent } from '../root/root.component';
import { UserService } from './user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  pwmodel: any = {};

  constructor(
    public root: RootComponent,
    private userService: UserService
  ) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.root.closeAlerts(this);
  }

  changePassword() {
    this.userService.changePassword(this.pwmodel.oldpassword, this.pwmodel.newpassword).
      subscribe(
        (b: boolean) => {
          this.root.addAlert({ type: 'success', message: 'password changed', parent: this });
        },
        (error) =>
          this.root.addErrorAlert(error, this)
      )
  }


}

