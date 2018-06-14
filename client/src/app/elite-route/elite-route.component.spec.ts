import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EliteRouteComponent } from './elite-route.component';

describe('EliteRouteComponent', () => {
  let component: EliteRouteComponent;
  let fixture: ComponentFixture<EliteRouteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EliteRouteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EliteRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
