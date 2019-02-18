import { Component, OnInit } from '@angular/core';
import { UserService } from '../user/user.service';
import { Role } from '../model/role';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  roles: string[];

  constructor(
    private userService: UserService
  ) { }

  ngOnInit() {
    this.userService.getRoles().subscribe((roles: Role[]) =>
      this.roles = roles.map(role => role.name)
    );
  }

}
