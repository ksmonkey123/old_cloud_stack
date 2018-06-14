import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class EliteRouteService {

  urls = {
    path: '/api/elite/path'
  };

  constructor(private http: HttpClient) { }

  findPath(from:string, to:string, range:number) {
    let params = new HttpParams()
                .set('from', from)
                .set('to', to)
                .set('distance', range.toString());
   
   return this.http.get<RouteData>(this.urls.path, {params});
  }

}

export interface RouteData {
   origin: string, 
   target: string,
   distance : number,
   routeLength : number,
   maxJumpRange: number,
   jumps: number,
   searchTime : number,
   steps: RouteStep[],
   cached: boolean;
}

export interface RouteStep {
  name: string,
  distance: number;
}