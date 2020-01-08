import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { Quiz } from 'src/app/models/quiz.model';

@Component({
  selector: 'app-finished-grade',
  templateUrl: './finished-grade.component.html',
  styleUrls: ['./finished-grade.component.css']
})
export class FinishedGradeComponent implements OnInit {

  grade: number;
  gradeSubscription: Subscription;

  currentQuiz: Quiz;
  currentQuizSubscription: Subscription;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.gradeSubscription = this.userService.grade$.subscribe(grade => {
      this.grade = grade;
    });

    this.currentQuizSubscription = this.userService.currentQuiz$.subscribe(quiz => {
      this.currentQuiz = quiz;
    });
  }

}
