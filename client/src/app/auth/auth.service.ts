import { HttpResponse } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule, Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export const TOKEN_NAME: string = 'jwt_token';
export const REFRESH_TOKEN_NAME: string = 'refresh_token';

@Injectable()
export class AuthService {

  hasToken(): boolean {
    return localStorage.getItem(TOKEN_NAME) !== null;
  }
  private url: string = '/auth/login';
  //private options = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

  constructor(private http: HttpClient) { }

  getToken(): string {
    return localStorage.getItem(TOKEN_NAME);
  }

  setToken(token: string): void {
    if (token === null)
      localStorage.removeItem(TOKEN_NAME)
    else
      localStorage.setItem(TOKEN_NAME, token);
  }
  getRefreshToken(): string {
    return localStorage.getItem(REFRESH_TOKEN_NAME);
  }

  setRefreshToken(token: string): void {
    if (token === null)
      localStorage.removeItem(REFRESH_TOKEN_NAME);
    else
      localStorage.setItem(REFRESH_TOKEN_NAME, token);
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post(
      this.url,
      JSON.stringify({ username: username, password: password })).pipe(
        map((response: any) => {
          // login successful if there's a jwt token in the response
          let accessToken = response.accessToken;
          let refreshToken = response.refreshToken;

          this.setToken(accessToken);
          this.setRefreshToken(refreshToken);

          // store username and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify({ username: username, token: response }));
          // return true to indicate successful login
          return true;
        }));
  }

  refreshTokens() {
    return this.http.post('/auth/refresh', null).
      pipe(map((response: any) => {
        // login successful if there's a jwt token in the response
        let accessToken = response.accessToken;
        let refreshToken = response.refreshToken;

        this.setToken(accessToken);
        this.setRefreshToken(refreshToken);

        // store username and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify({
          username: JSON.parse(localStorage.getItem('currentUser')).username,
          token: response
        }));
        // return true to indicate successful login
        return true;
      }));
  }

  fullLogout(callback: () => void) {
    this.http.post('/auth/doLogout', null).subscribe(
      x => {
        this.logout();
        callback();
      }
      ,
      error => {
        this.logout();
        callback();
      });
  }

  logout(): void {
    this.setToken(null);
    this.setRefreshToken(null);
    localStorage.removeItem('currentUser');
  }
}
