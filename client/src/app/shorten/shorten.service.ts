import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ShortenEntry } from './shortenEntry';

@Injectable()
export class ShortenService {

  listUrl = '/api/shorten/list';

  constructor(private http: HttpClient) { }

  getList(): Observable<ShortenEntry[]> {
    return this.http.get<ShortenEntry[]>(this.listUrl);
  }

}
