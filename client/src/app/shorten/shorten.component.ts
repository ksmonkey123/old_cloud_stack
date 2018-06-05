import { Component, OnInit } from '@angular/core';
import { ShortenService } from './shorten.service';
import { ShortenEntry } from './shortenEntry';

@Component({
  selector: 'app-shorten',
  templateUrl: './shorten.component.html',
  styleUrls: ['./shorten.component.css']
})
export class ShortenComponent implements OnInit {

  constructor(private shortenService : ShortenService) { }

  links : ShortenEntry[] = [];

  ngOnInit() {
    this.shortenService.getList().subscribe(
      (list : ShortenEntry[]) => {
        this.links = list;
      },
      (error : Error) => {
        console.log(error);
      }
    )
  }

}
