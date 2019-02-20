import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { routerNgProbeToken } from '@angular/router/src/router_module';

@Component({
  selector: 'app-ytdl',
  templateUrl: './ytdl.component.html',
  styleUrls: ['./ytdl.component.css']
})
export class YtdlComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    if (this.route.firstChild == null) {
      this.router.navigate(['ytdl/list'])
    }
  }

}
