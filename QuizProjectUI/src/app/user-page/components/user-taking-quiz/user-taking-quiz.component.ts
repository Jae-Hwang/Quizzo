import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Subscription } from 'rxjs';
import { Quiz } from 'src/app/models/quiz.model';
import { Question } from 'src/app/models/question.model';

@Component({
  selector: 'app-user-taking-quiz',
  templateUrl: './user-taking-quiz.component.html',
  styleUrls: ['./user-taking-quiz.component.css']
})
export class UserTakingQuizComponent implements OnInit {

  currentQuiz: Quiz;
  currentQuizSubscription: Subscription;

  questions: Question[];
  questionsSubscription: Subscription;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.currentQuizSubscription = this.userService.currentQuiz$.subscribe(quiz => {
      this.currentQuiz = quiz;
      this.userService.getQuestions(quiz.qid);
    });

    this.questionsSubscription = this.userService.questions$.subscribe(questions => {
      this.questions = questions;
    });
  }

  questionType(type: number) {
    if (type === 1) {
      return '   Multiple-Choice';
    } else if (type === 2) {
      return '   True-False';
    } else if (type === 3) {
      return '   Short Answer';
    }
  }

}
