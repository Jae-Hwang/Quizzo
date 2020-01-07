import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { Subscription } from 'rxjs';
import { AppUser } from 'src/app/models/app.user.model';
import { Quiz } from 'src/app/models/quiz.model';
import { Router } from '@angular/router';

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

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router) { }

  ngOnInit() {
    this.currentUserSubscription = this.authService.$currentUser.subscribe(user => {
      this.currentUser = user;
      this.userService.getUserQuizzes(user.userid);
    });

    this.userQuizzesSubscription = this.userService.userQuizzes$.subscribe(quizzes => {
      this.userQuizzes = quizzes;
    });
  }

  clickQuiz(quiz: Quiz) {
    console.log(quiz);
    this.userService.setCurrentQuiz(quiz);
    this.router.navigateByUrl('/user/quiz');
  }

}
