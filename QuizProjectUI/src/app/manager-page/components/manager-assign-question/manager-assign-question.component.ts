import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ManagerService } from 'src/app/services/manager.service';
import { Quiz } from 'src/app/models/quiz.model';
import { Question } from 'src/app/models/question.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-manager-assign-question',
  templateUrl: './manager-assign-question.component.html',
  styleUrls: ['./manager-assign-question.component.css']
})
export class ManagerAssignQuestionComponent implements OnInit {

  currentQuiz: Quiz;

  quizzes: Quiz[];
  quizzesSubscription: Subscription;

  assignedQuestions: Question[];
  assignedQuestionsSub: Subscription;

  allQuestions: Question[];
  allQuestionsSub: Subscription;

  questionCurrentPage = 1;
  questionPageArray: number[];
  questionPage: number;
  questionPageSubscription: Subscription;

  quizCurrentPage = 1;
  quizPageArray: number[];
  quizPage: number;
  quizPageSubscription: Subscription;

  constructor(private managerService: ManagerService) { }

  ngOnInit() {
    this.quizzesSubscription = this.managerService.allQuizzes$.subscribe(quizzes => {
      this.quizzes = quizzes;
    });

    this.quizPageSubscription = this.managerService.quizzesPage$.subscribe(page => {
      this.quizPage = page;
      this.quizPageArray = new Array<number>();
      for (let i = 1; i <= page; i++) {
        this.quizPageArray.push(i);
      }
    });

    this.assignedQuestionsSub = this.managerService.quizQuestions$.subscribe(questions => {
      this.assignedQuestions = questions;
    });

    this.allQuestionsSub = this.managerService.allQuestions$.subscribe(questions => {
      this.allQuestions = questions;
    });

    this.questionPageSubscription = this.managerService.questionsPage$.subscribe(page => {
      this.questionPage = page;
      this.questionPageArray = new Array<number>();
      for (let i = 1; i <= page; i++) {
        this.questionPageArray.push(i);
      }
    });


    this.managerService.getAllQuizzes(1);
  }

  clickQuiz(quiz: Quiz) {
    this.currentQuiz = quiz;
    this.managerService.getQuizQuestions(quiz.qid);
    this.managerService.getAllQuestions(1);
  }

  resetCurrentQuiz() {
    this.currentQuiz = null;
    this.quizCurrentPage = 1;
  }

  setQuizPage(page: number) {
    this.quizCurrentPage = page;
    console.log(page);
    this.managerService.getAllQuizzes(page);
  }

  setQuestionPage(page: number) {
    this.questionCurrentPage = page;
    console.log(page);
    this.managerService.getAllQuestions(page);
  }

}
