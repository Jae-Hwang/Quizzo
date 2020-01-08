import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignQuizSelectComponent } from './assign-quiz-select.component';

describe('AssignQuizSelectComponent', () => {
  let component: AssignQuizSelectComponent;
  let fixture: ComponentFixture<AssignQuizSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignQuizSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignQuizSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
