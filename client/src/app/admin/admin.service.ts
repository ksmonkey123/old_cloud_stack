import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';
import { Role } from '../model/role';
import { map } from 'rxjs/operators';

@Injectable()
export class AdminService {

  urls = {
    roles: '/api/auth/roles',
    users: '/api/auth/users',
    patchRoles: (id: number) => `/api/auth/user/${id}/role`,
    user: (id: number) => `/api/auth/user/${id}`,
    postPassword: (id: number) => `/api/auth/user/${id}/password`,
    postUser: '/api/auth/user'
  };

  constructor(private http: HttpClient) { }

  getRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(this.urls.roles);
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.urls.users);
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(this.urls.user(id));
  }

  patchRoles(id: number, roles: string[]): Observable<User[]> {
    return this.http.patch<User[]>(this.urls.patchRoles(id), { roles: roles });
  }

  updateUserPassword(id: number, pw: string): Observable<boolean> {
    return this.http.post<any>(this.urls.postPassword(id), { password: pw }).pipe(map(x => true));
  }

  createUser(username: string, password: string): Observable<boolean> {
    return this.http.post<any>(this.urls.postUser, { username: username, password: password })
      .pipe(map(x => true));
  }

  deleteUser(id: number): Observable<boolean> {
    return this.http.delete<any>(this.urls.user(id)).pipe(map(x => true));
  }

}
