import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTakingQuizComponent } from './user-taking-quiz.component';

describe('UserTakingQuizComponent', () => {
  let component: UserTakingQuizComponent;
  let fixture: ComponentFixture<UserTakingQuizComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserTakingQuizComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserTakingQuizComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
