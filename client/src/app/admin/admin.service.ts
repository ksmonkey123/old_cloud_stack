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
    users: 'api/auth/users',
    patchRoles: (id: number) => `api/auth/user/${id}/role`
  };

  constructor(private http: HttpClient) { }

  getRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(this.urls.roles);
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.urls.users);
  }

  patchRoles(id: number, roles: string[]): Observable<User[]> {
    return this.http.patch<User[]>(this.urls.patchRoles(id), {roles: roles});
  }

}
