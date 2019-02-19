import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NetcodeApp } from './netcodeApp';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NetcodeService {

  urls = {
    list: '/api/netcode/list',
    create: '/api/netcode/app',
    delete: (id: string) => `/api/netcode/app/${id}`
  };

  constructor(private http: HttpClient) { }

  getAppList(): Observable<NetcodeApp[]> {
    return this.http.get<NetcodeApp[]>(this.urls.list);
  }

  createApp(name: string): Observable<boolean> {
    return this.http.post<any>(this.urls.create, { name: name }).pipe(map(x => true));
  }

  deleteApp(id: string): Observable<boolean> {
    return this.http.delete<any>(this.urls.delete(id)).pipe(map(x => true));
  }

}
