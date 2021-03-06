import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Format, JobSummary, FormatListEntry, JobDetails } from './ytdl.model';

@Injectable()
export class YtdlService {

  private urls = {
    list: "/api/ytdl/list",
    formats: "/api/ytdl/formats",
    addJob: "/api/ytdl/job",
    jobDetails: (id: number) => `/api/ytdl/job/${id}`,
    retryJob: (id: number) => `/api/ytdl/job/${id}/retry`
  }

  constructor(private http: HttpClient) { }

  getFormatList(): Observable<FormatListEntry[]> {
    return this.http.get<FormatListEntry[]>(this.urls.formats);
  }

  postJob(url: string, formats: number[]):Observable<boolean> {
    return this.http.post<any>(this.urls.addJob, {url: url, formats: formats}).pipe(map(x => true))
  }

  getJobList(): Observable<JobSummary[]> {
    return this.http.get<JobSummary[]>(this.urls.list);
  }

  getJobDetails(jobId: number): Observable<JobDetails> {
    return this.http.get<JobDetails>(this.urls.jobDetails(jobId));
  }

  deleteJob(jobId: number): Observable<boolean> {
    return this.http.delete<any>(this.urls.jobDetails(jobId)).pipe(map(x => true));
  }

  retryJob(jobId: number): Observable<boolean> {
    return this.http.post<any>(this.urls.retryJob(jobId), []).pipe(map(x => true));
  }

}
