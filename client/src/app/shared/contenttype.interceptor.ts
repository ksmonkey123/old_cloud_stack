import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class ContentTypeInterceptor implements HttpInterceptor {

  constructor() {}

  exclude = [/^\/api\/case\/[0-9]+\/file$/, /^\/api\/file\/[0-9]+\/content$/];

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let skip = false;
    this.exclude.forEach((ex) => {
      if(ex.test(request.url)){
        skip = true;
      }
    });

    if(!skip){
      const req = request.clone({
        headers: request.headers.set('Content-Type', 'application/json')
      })
      return next.handle(req);
    }
    return next.handle(request);
  }
}
