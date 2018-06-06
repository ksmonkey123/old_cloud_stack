import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ShortenEntry } from './shortenEntry';

@Injectable()
export class ShortenService {

  urls = {
    list: '/api/shorten/list',
    patch: (id : string) => `/api/shorten/link/${id}`,
    create: '/api/shorten/link'
  };

  constructor(private http: HttpClient) { }

  getList(): Observable<ShortenEntry[]> {
    return this.http.get<ShortenEntry[]>(this.urls.list);
  }

  patchLink(link: ShortenEntry) {
    return this.http.patch(this.urls.patch(link.identifier), link);
  }

  addLink(target : string) {
    return this.http.post<ShortenEntry>(this.urls.create, {target: target});
  }

}
