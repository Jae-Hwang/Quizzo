import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerCreateQuizComponent } from './manager-create-quiz.component';

describe('ManagerCreateQuizComponent', () => {
  let component: ManagerCreateQuizComponent;
  let fixture: ComponentFixture<ManagerCreateQuizComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManagerCreateQuizComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagerCreateQuizComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
