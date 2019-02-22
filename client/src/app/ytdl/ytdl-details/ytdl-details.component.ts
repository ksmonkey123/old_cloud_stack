import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { YtdlService } from '../ytdl.service';

@Component({
  selector: 'app-ytdl-details',
  templateUrl: './ytdl-details.component.html',
  styleUrls: ['./ytdl-details.component.css']
})
export class YtdlDetailsComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private service: YtdlService
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      var jobId = Number.parseInt(params.get('id'));
      console.log('jobId: ' + jobId);
    });
  }

}
