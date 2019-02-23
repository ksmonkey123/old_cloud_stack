import { Component, OnInit } from '@angular/core';
import { PARAMETERS } from '@angular/core/src/util/decorators';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { User } from 'src/app/model/user';
import { AdminService } from '../admin.service';
import { UserService } from 'src/app/user/user.service';
import { RootComponent, DangerModalData } from 'src/app/root/root.component';
import { AdminComponent } from '../admin.component';

@Component({
  selector: 'app-user-admin',
  templateUrl: './user-admin.component.html',
  styleUrls: ['./user-admin.component.css']
})
export class UserAdminComponent implements OnInit {

  user: User;
  pwmodel: any = {};
  closeResult: string;

  constructor(
    private route: ActivatedRoute,
    private adminService: AdminService,
    private userService: UserService,
    private router: Router,
    private root: RootComponent,
    private parent: AdminComponent
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      var userId = Number.parseInt(params.get('id'));
      this.userService.getUserInfo().subscribe(u => {
        if (u.id == userId)
          this.router.navigate(['/admin']);
        this.loadUser(userId);
      });
    });
  }

  loadUser(userId: number) {
    this.adminService.getUser(userId).subscribe(
      u => this.user = u,
      error => this.router.navigate(['/admin']));
  }

  ngOnDestroy() {
    this.root.closeAlerts(this);
  }

  changePassword() {
    this.adminService.updateUserPassword(this.user.id, this.pwmodel.newpassword).
      subscribe(
        (b: boolean) => {
          this.root.addAlert({ type: 'success', message: 'password changed', parent: this });
        },
        (error) =>
          this.root.addErrorAlert(error, this)
      )
  }

  deleteUser() {
    this.root.openDangerModal(new DangerModalData("Delete User"), () =>
      this.doDelete());
  }

  doDelete() {
    this.adminService.deleteUser(this.user.id).subscribe(
      (b: boolean) => {
        this.root.addAlert({ type: 'success', message: 'user deleted', parent: this.parent });
        this.router.navigate(['/admin']);
        this.parent.ngOnInit();
      },
      (error) =>
        this.root.addErrorAlert(error, this)
    )
  }

}
