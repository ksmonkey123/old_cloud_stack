import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';
import { Role } from '../model/role';
import { map } from 'rxjs/operators';

@Injectable()
export class UserService {

  urls = {
    info: '/api/auth/me',
    password: '/api/auth/password'
  };

  constructor(private http: HttpClient) { }

  getUserInfo(): Observable<User> {
    return this.http.get<User>(this.urls.info);
  }

  changePassword(oldPW: string, newPW: string): Observable<boolean> {
    return this.http.post(this.urls.password, { password: oldPW, newPassword: newPW })
      .pipe(
        map(x => true));
  }

}
