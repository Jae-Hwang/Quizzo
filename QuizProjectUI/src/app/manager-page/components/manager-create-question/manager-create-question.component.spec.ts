import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerCreateQuestionComponent } from './manager-create-question.component';

describe('ManagerCreateQuestionComponent', () => {
  let component: ManagerCreateQuestionComponent;
  let fixture: ComponentFixture<ManagerCreateQuestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManagerCreateQuestionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagerCreateQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
