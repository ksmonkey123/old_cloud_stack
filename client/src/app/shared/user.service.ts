import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';

@Injectable()
export class UserService {

  userInfoUrl = '/auth/me';

  constructor(private http: HttpClient) { }

  getUserInfo(): Observable<User> {
    return this.http.get<User>(this.userInfoUrl);
  }

}
