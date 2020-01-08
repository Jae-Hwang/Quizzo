import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinishedGradeComponent } from './finished-grade.component';

describe('FinishedGradeComponent', () => {
  let component: FinishedGradeComponent;
  let fixture: ComponentFixture<FinishedGradeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinishedGradeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinishedGradeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
