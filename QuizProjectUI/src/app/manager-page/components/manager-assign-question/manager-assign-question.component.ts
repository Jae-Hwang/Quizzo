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

  quizPage: number;
  quizPageSubscription: Subscription;

  constructor(private managerService: ManagerService) { }

  ngOnInit() {
    this.quizzesSubscription = this.managerService.quizzes$.subscribe(quizzes => {
      this.quizzes = quizzes;
    });

    this.quizPageSubscription = this.managerService.quizzesPage$.subscribe(page => {
      this.quizPage = page;
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


    this.managerService.getQuizzes(1);
  }

  clickQuiz(quiz: Quiz) {
    this.currentQuiz = quiz;
    this.managerService.getQuizQuestions(quiz.qid);
    this.managerService.getAllQuestions(1);
  }

  setQuestionPage(page: number) {
    this.questionCurrentPage = page;
    console.log(page);
    this.managerService.getAllQuestions(page);
  }

}
