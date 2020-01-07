import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerAssignUserComponent } from './manager-assign-user.component';

describe('ManagerAssignUserComponent', () => {
  let component: ManagerAssignUserComponent;
  let fixture: ComponentFixture<ManagerAssignUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManagerAssignUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagerAssignUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
