import { Component, OnInit } from '@angular/core';
import { EliteRouteService, RouteData } from './elite-route.service';

@Component({
  selector: 'app-elite-route',
  templateUrl: './elite-route.component.html',
  styleUrls: ['./elite-route.component.css']
})
export class EliteRouteComponent implements OnInit {

  from: string;
  to: string;
  range: number = 10;
  result: RouteData | null = null;

  constructor(private routeService: EliteRouteService) { }

  ngOnInit() {
  }

  findRoute() {
    this.routeService.findPath(this.from, this.to, this.range).subscribe(
       (route: RouteData) => {
         this.result = route;
        },
        error => console.log(error)
      );
  }

}
