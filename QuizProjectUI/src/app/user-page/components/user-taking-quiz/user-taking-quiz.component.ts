import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Subscription } from 'rxjs';
import { Quiz } from 'src/app/models/quiz.model';
import { Question } from 'src/app/models/question.model';
import { AuthService } from 'src/app/services/auth.service';
import { AppUser } from 'src/app/models/app.user.model';

@Component({
  selector: 'app-user-taking-quiz',
  templateUrl: './user-taking-quiz.component.html',
  styleUrls: ['./user-taking-quiz.component.css']
})
export class UserTakingQuizComponent implements OnInit {

  currentUser: AppUser;
  currentUserSubscription: Subscription;

  currentQuiz: Quiz;
  currentQuizSubscription: Subscription;

  questions: Question[];
  questionsSubscription: Subscription;

  currentQuestion: Question;

  answers: Map<number, string>; // qid, answer

  selected: string;
  submitText = 'Submit';

  constructor(private userService: UserService, private authService: AuthService) { }

  ngOnInit() {
    this.currentUserSubscription = this.authService.$currentUser.subscribe(user => {
      this.currentUser = user;
    });

    this.currentQuizSubscription = this.userService.currentQuiz$.subscribe(quiz => {
      this.currentQuiz = quiz;
      this.userService.getQuestions(quiz.qid);
    });

    this.questionsSubscription = this.userService.questions$.subscribe(questions => {
      this.questions = questions;
      this.currentQuestion = questions[0];
      this.submitText = 'Submit';
      this.answers = new Map();
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

  answeredButton(question: Question) {
    let btnClass = 'btn btn-secondary';
    if (this.answers.has(question.quid)) {
      btnClass = 'btn btn-warning';
    }
    return btnClass;
  }

  clickQuestion(question: Question) {
    this.currentQuestion = question;
    this.selected = null;
    this.submitText = 'Submit';
  }

  select(selected: string) {
    this.selected = selected;
  }

  selectedOption(selected: string) {
    let selectClass = 'bg-light';
    if (this.selected === selected) {
      selectClass = 'bg-secondary text-white';
    }
    return selectClass;
  }

  submit() {
    console.log(this.selected);
    this.answers.set(this.currentQuestion.quid, this.selected.replace(/[^a-zA-z0-9 ]/g, ''));
    this.submitText = 'Submitted';
  }

  submitQuiz() {
    let result = '';
    this.answers.forEach((value, key) => {
      result = result.concat(`${key}:${value},`);
    });
    result = result.replace(/,$/, '');
    console.log(result);
    this.userService.submitQuiz(this.currentUser.userid, this.currentQuiz.qid, result);
  }
}
 