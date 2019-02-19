import { Component, OnInit } from '@angular/core';
import { NetcodeApp } from './netcodeApp';
import { NetcodeService } from './netcode.service';
import { RootComponent } from '../root/root.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-netcode',
  templateUrl: './netcode.component.html',
  styleUrls: ['./netcode.component.css']
})
export class NetcodeComponent implements OnInit {

  apps: NetcodeApp[];
  newApp: string = '';

  constructor(
    private service: NetcodeService,
    private root: RootComponent,
    private modalService: NgbModal
  ) { }

  ngOnInit() {
    this.service.getAppList().subscribe(apps => this.apps = apps);
  }

  addApp() {
    if (this.newApp.length == 0)
      return;
    this.service.createApp(this.newApp).subscribe(x => {
      this.root.closeAlerts(this);
      this.root.addSuccessAlert("app created", this);
      this.ngOnInit();
    },
      (error) => this.root.addErrorAlert(error, this));
  }

  open(content: any, id: string) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.doDelete(id);
    }, (reason) => {
    });
  }

  doDelete(id: string) {
    this.service.deleteApp(id).subscribe(
      x => {
        this.root.closeAlerts(this);
        this.root.addSuccessAlert("app deleted", this);
        this.ngOnInit();
      },
      error => this.root.addErrorAlert(error, this)
    )
  }

}
