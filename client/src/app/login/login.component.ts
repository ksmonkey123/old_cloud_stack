import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private auth: AuthService, private router: Router) { }

  model: any = {};
  problem = false;

  ngOnInit() {
    if (this.auth.hasToken()) {
      console.log('redirecting');
      this.router.navigate(['/'])
    }
  }

  onSubmitClicked() {
    this.auth.login(this.model.username, this.model.password)
      .subscribe(
        result => {
          this.router.navigate(['/']);
        },
        error => {
          this.problem = true;
        });
  }

  closeAlert() {
   this.problem = false;
  }

}
