import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerAssignQuestionComponent } from './manager-assign-question.component';

describe('ManagerAssignQuestionComponent', () => {
  let component: ManagerAssignQuestionComponent;
  let fixture: ComponentFixture<ManagerAssignQuestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManagerAssignQuestionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagerAssignQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
