import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { YtdlService } from '../ytdl.service';
import { JobDetails, JobBase } from '../ytdl.model';
import { RootComponent, DangerModalData } from 'src/app/root/root.component';

@Component({
  selector: 'app-ytdl-details',
  templateUrl: './ytdl-details.component.html',
  styleUrls: ['./ytdl-details.component.css']
})
export class YtdlDetailsComponent implements OnInit, OnDestroy {

  jobId: number;

  job: JobDetails;
  timer: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private root: RootComponent,
    private service: YtdlService
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.jobId = Number.parseInt(params.get('id'));
      this.loadData();
      this.timer = setInterval(() => this.loadData(), 5000);
    });
  }

  ngOnDestroy() {
    clearInterval(this.timer);
  }

  loadData() {
    this.service.getJobDetails(this.jobId).subscribe(
      job => this.job = job,
      error => this.router.navigate(['/ytdl/list'])
    );
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

  getProgressBarClass(job: JobDetails): string {
    switch (job.status) {
      case 'PENDING': return 'progress-bar-striped progress-bar-animated bg-secondary';
      case 'CONVERTING':
      case 'DOWNLOADING': return 'progress-bar-striped progress-bar-animated bg-primary';
      case 'FAILED': return 'bg-danger';
      case 'DONE': return 'bg-success';
    }
  }

  getProgressBarStyle(job: JobDetails) {
    switch (job.status) {
      case 'PENDING':
      case 'DONE':
      case 'FAILED':
        return { width: '100%' };
      case 'CONVERTING':
        return { width: (100 * (job.files.length + 2) / (job.formats.length + 1)).toFixed(0) + '%' }
      case 'DOWNLOADING':
        return { width: (100 / (job.formats.length + 1)).toFixed(0) + '%' }
    }
  }

  prettyFileSize(size: number): string {
    if (size < 1000)
      return size + " B";
    if (size < 1000000)
      return (size / 1000).toPrecision(3) + " kB";
    if (size < 1000000000)
      return (size / 1000000).toPrecision(3) + " MB";
    return (size / 1000000000).toPrecision(3) + " GB";
  }

  deleteJob(job: JobBase) {
    this.root.openDangerModal(new DangerModalData('Delete Job #' + job.id), () =>
      this.service.deleteJob(job.id).subscribe(
        x => this.loadData(),
        e => this.root.addErrorAlert(e, this)
      ));
  }

  retryJob(job: JobBase) {
    this.service.retryJob(job.id).subscribe(
      x => this.loadData(),
      e => this.root.addErrorAlert(e, this)
    )
  }

}
