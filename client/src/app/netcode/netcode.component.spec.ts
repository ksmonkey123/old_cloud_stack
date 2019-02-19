import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetcodeComponent } from './netcode.component';

describe('NetcodeComponent', () => {
  let component: NetcodeComponent;
  let fixture: ComponentFixture<NetcodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetcodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetcodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
