import { Component, OnInit } from '@angular/core';
import { RootComponent } from 'src/app/root/root.component';
import { AdminService } from '../admin.service';
import { Router } from '@angular/router';
import { AdminComponent } from '../admin.component';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  userModel: any = {};

  constructor(
    private root: RootComponent,
    private service: AdminService,
    private admin: AdminComponent,
    private router: Router
  ) { }

  ngOnInit() {
  }

  createUser() {
    this.service.createUser(this.userModel.username, this.userModel.password).subscribe(
      (b: boolean) => {
        this.root.addAlert({ type: 'success', message: 'user created', parent: this });
        this.router.navigate(['/admin']);
        this.admin.ngOnInit();
      },
      (error) =>
        this.root.addErrorAlert(error, this)
    )
  }

}
