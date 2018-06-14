import { Component, OnInit, NgZone, ChangeDetectorRef } from '@angular/core';
import { EliteRouteService, RouteData } from './elite-route.service';
import { RootComponent } from '../root/root.component';

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
  working: boolean = false;
  error: boolean = false;
  cached: boolean = false;

  constructor(private routeService: EliteRouteService, private root: RootComponent, private ref: ChangeDetectorRef) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.root.closeAlerts(this);
  }

  findRoute() {
    this.result = null;
    this.error = false;
    this.working = true;
    this.routeService.findPath(this.from, this.to, this.range).subscribe(
       (route: RouteData) => {
          this.working = false;
          this.root.closeAlerts(this);
          this.result = route;
          this.cached = route.cached;
          this.ref.detectChanges();
        },
        error => {
          this.error = true;
          this.working = false;
          this.root.addErrorAlert(error, this);
        }
      );
  }

}
