import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignQuestionSelectComponent } from './assign-question-select.component';

describe('AssignQuestionSelectComponent', () => {
  let component: AssignQuestionSelectComponent;
  let fixture: ComponentFixture<AssignQuestionSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignQuestionSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignQuestionSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
