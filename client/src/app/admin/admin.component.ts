import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';
import { Role } from '../model/role';
import { User } from '../model/user';
import { UserService } from '../user/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  roles: Role[];
  users: User[];

  busy: number = 3;
  currentUser: User;

  constructor(
    private service: AdminService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.userService.getUserInfo().subscribe(u => {
      this.currentUser = u;
      this.busy--;
    });
    this.service.getRoles().subscribe(roles => {
      this.roles = roles;
      this.busy--;
    });
    this.service.getUsers().subscribe(users => {
      this.users = users;
      this.busy--;
    });
  }

  updateRole(user: User, role: Role) {
    this.busy++;
    if (user.roles.includes(role.role))
      user.roles = user.roles.filter(r => r != role.role);
    else
      user.roles.push(role.role);

    this.service.patchRoles(user.id, user.roles).subscribe(users => {
      this.users = users;
      this.busy--;
    });
  }

}
