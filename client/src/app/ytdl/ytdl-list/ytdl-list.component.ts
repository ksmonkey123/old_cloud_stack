import { Component, OnInit, OnDestroy } from '@angular/core';
import { YtdlService } from '../ytdl.service';
import { Format, FormatListEntry, JobSummary } from '../ytdl.model';
import { RootComponent } from 'src/app/root/root.component';

@Component({
  selector: 'app-ytdl-list',
  templateUrl: './ytdl-list.component.html',
  styleUrls: ['./ytdl-list.component.css']
})
export class YtdlListComponent implements OnInit, OnDestroy {

  constructor(
    private service: YtdlService,
    private root: RootComponent
  ) { }

  formats: FormatListEntry[] = [];
  jobs: JobSummary[] = [];
  model = new AddJobFormModel();
  timer: any;

  ngOnInit() {
    this.service.getFormatList().subscribe(f => {
      this.formats = f;
    });
    this.loadList();
    this.timer = setInterval(() => this.loadList(), 5000);
  }

  ngOnDestroy() {
    clearInterval(this.timer);
  }

  onAdd() {
    this.service.postJob(this.model.url, this.model.getSelectedFormats()).subscribe(
      x => this.postAdd(),
      error => this.root.addErrorAlert(error, this)
    )
  }

  postAdd() {
    this.root.addSuccessAlert('job added to queue', this);
    this.model = new AddJobFormModel();
    this.loadList();
  }

  loadList() {
    this.service.getJobList().subscribe(jobs => this.jobs = jobs)
  }

}

class AddJobFormModel {
  url: string;
  formats: boolean[];

  constructor() {
    this.url = '';
    this.formats = [];
  }

  getSelectedFormats(): number[] {
    return this.formats.reduce((o, v, i) => v ? o.concat(i) : o, []);
  }

}