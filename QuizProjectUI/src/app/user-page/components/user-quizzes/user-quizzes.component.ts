import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { Subscription } from 'rxjs';
import { AppUser } from 'src/app/models/app.user.model';
import { Quiz } from 'src/app/models/quiz.model';
import { Router } from '@angular/router';
import { Result } from 'src/app/models/result.model';

@Component({
  selector: 'app-user-quizzes',
  templateUrl: './user-quizzes.component.html',
  styleUrls: ['./user-quizzes.component.css']
})
export class UserQuizzesComponent implements OnInit {

  currentUser: AppUser;
  currentUserSubscription: Subscription;

  userQuizzes: Quiz[];
  userQuizzesSubscription: Subscription;

  results: Result[];
  resultsSubscription: Subscription;

  displayGrade: number;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router) { }

  ngOnInit() {
    this.currentUserSubscription = this.authService.$currentUser.subscribe(user => {
      this.currentUser = user;
      this.userService.getUserQuizzes(user.userid);
      this.userService.getResultsByUserid(user.userid);
    });

    this.userQuizzesSubscription = this.userService.userQuizzes$.subscribe(quizzes => {
      this.userQuizzes = quizzes;
    });

    this.resultsSubscription = this.userService.results$.subscribe(results => {
      this.results = results;
    });
  }

  clickQuiz(quiz: Quiz) {
    if (!this.isFinished(quiz)) {
      console.log(quiz);
      this.userService.setCurrentQuiz(quiz);
      this.router.navigateByUrl('/user/quiz');
    }
  }

  isFinished(quiz: Quiz) {
    let finished = false;
    this.results.forEach(result => {
      if (result.qid === quiz.qid) {
        finished = true;
      }
    });
    return finished;
  }

  resultModal(qid: number) {
    return `resultModal${qid}`;
  }

  resultModalTarget(qid: number) {
    return `#resultModal${qid}`;
  }

  getGradeFromQid(qid: number) {
    this.results.forEach(result => {
      if (qid === result.qid) {
        this.displayGrade = result.grade;
      }
    });
  }
}
