import { Component, OnInit } from '@angular/core';
import { ShortenService } from './shorten.service';
import { ShortenEntry } from './shortenEntry';

@Component({
  selector: 'app-shorten',
  templateUrl: './shorten.component.html',
  styleUrls: ['./shorten.component.css']
})
export class ShortenComponent implements OnInit {

  constructor(private shortenService: ShortenService) { }

  links: ShortenEntry[] = [];
  addURL: string = null;

  ngOnInit() {
    this.shortenService.getList().subscribe(
      (list: ShortenEntry[]) => {
        this.links = list;
      },
      (error: Error) => {
        console.log(error);
      }
    )
  }

  updateLink(link: ShortenEntry) {
    link.active = !link.active;
    this.shortenService.patchLink(link).subscribe(
      x => {
        console.log("patch done");
      },
      error => {
        console.log("patch failed");
        console.log(error);
      }
    )
  }

  addLink() {
    if (!(this.addURL.startsWith("http://") || this.addURL.startsWith("https://"))) {
      this.addURL = `http://${this.addURL}`;
    }

    this.shortenService.addLink(this.addURL).subscribe(
      link => {
        this.links.unshift(link);
        this.addURL = null;
      },
      error => console.log(error)
    );
  }

}
