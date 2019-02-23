import { Component, OnInit, OnDestroy } from '@angular/core';
import { YtdlService } from '../ytdl.service';
import { Format, FormatListEntry, JobSummary } from '../ytdl.model';
import { RootComponent } from 'src/app/root/root.component';
import { NgStyle } from '@angular/common';

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
    this.model = new AddJobFormModel();
    this.loadList();
  }

  loadList() {
    this.service.getJobList().subscribe(jobs => this.jobs = jobs)
  }

  getClassForStatus(status: string) {
    switch (status) {
      case 'PENDING': return 'secondary';
      case 'DOWNLOADING': return 'primary';
      case 'CONVERTING': return 'primary';
      case 'FAILED': return 'danger';
      case 'DONE': return 'success';
    }
  }

  getProgressBarClass(job: JobSummary): string {
    switch (job.status) {
      case 'PENDING': return 'progress-bar-striped progress-bar-animated bg-secondary';
      case 'CONVERTING':
      case 'DOWNLOADING': return 'progress-bar-striped progress-bar-animated bg-primary';
      case 'FAILED': return 'bg-danger';
      case 'DONE': return 'bg-success';
    }
  }

  getProgressBarStyle(job: JobSummary) {
    switch (job.status) {
      case 'PENDING':
      case 'DONE':
      case 'FAILED':
        return { width: '100%' };
      case 'CONVERTING':
        return { width: (100 * (job.files + 2) / (job.formats + 1)).toFixed(0) + '%' }
      case 'DOWNLOADING':
        return { width: (100 / (job.formats + 1)).toFixed(0) + '%' }
    }
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