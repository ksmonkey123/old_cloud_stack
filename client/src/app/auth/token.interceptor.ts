import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { AuthService } from './auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subject, Observable, EMPTY, throwError } from 'rxjs';
import { switchMap, catchError, tap } from 'rxjs/operators';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private router: Router, public auth: AuthService) { }

  refreshTokenInProgress = false;

  tokenRefreshedSource = new Subject();
  tokenRefreshed$ = this.tokenRefreshedSource.asObservable();

  refreshToken() {
    if (this.refreshTokenInProgress) {
      return new Observable(observer => {
        this.tokenRefreshed$.subscribe(() => {
          observer.next();
          observer.complete();
        });
      });
    } else {
      this.refreshTokenInProgress = true;

      return this.auth.refreshTokens()
        .pipe(tap(() => {
          this.refreshTokenInProgress = false;
          this.tokenRefreshedSource.next();
        }));
    }
  }

  logout() {
    console.log("logout");
    this.auth.logout();
    this.router.navigate(['login']);
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url === '/api/auth/refresh' || request.url === '/api/auth/doLogout') {
      // auth/refresh and auth/doLogout are authenticated using the refresh token
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.auth.getRefreshToken()}`
        }
      });
      return next.handle(request);
    } else {
      if (request.url !== '/api/auth/login') {
        // auth/login is unauthenticated, everything else uses access token
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${this.auth.getToken()}`
          }
        });
        return next.handle(request).pipe(catchError(error => {
          console.log('request failed');
          if (error.status === 401) {
            return this.refreshToken()
              .pipe(
                switchMap(() => {
                  console.log('retrying request');
                  request = request.clone({
                    setHeaders: {
                      Authorization: `Bearer ${this.auth.getToken()}`
                    }
                  });
                  return next.handle(request);
                }),
                catchError(e2 => {
                  console.log('retry failed');
                  if (e2.status === 401) {
                    this.logout();
                    return EMPTY;
                  } else {
                    throw e2;
                  }
                }));
          }
          return throwError(error);
        }));
      } else {
        // auth/login uses default behaviour
        return next.handle(request);
      }
    }
  }
}
