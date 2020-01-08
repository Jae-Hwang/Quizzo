import { Component, OnInit } from '@angular/core';
import { AppUser } from 'src/app/models/app.user.model';
import { ManagerService } from 'src/app/services/manager.service';
import { AuthService } from 'src/app/services/auth.service';
import { Subscription } from 'rxjs';
import { Quiz } from 'src/app/models/quiz.model';

@Component({
  selector: 'app-manager-assign-user',
  templateUrl: './manager-assign-user.component.html',
  styleUrls: ['./manager-assign-user.component.css']
})
export class ManagerAssignUserComponent implements OnInit {

  currentUser: AppUser;

  allUsers: AppUser[];
  allUsersSubscription: Subscription;

  userCurrentPage = 1;
  userPageArray: number[];
  userPage: number;
  userPageSubscription: Subscription;

  assignedQuizzes: Quiz[];
  assignedQuizzesSubscription: Subscription;

  allQuizzes: Quiz[];
  allQuizzesSubscription: Subscription;

  quizCurrentPage = 1;
  quizPageArray: number[];
  quizPage: number;
  quizPageSubscription: Subscription;


  constructor(private managerService: ManagerService, private authService: AuthService) { }

  ngOnInit() {
    this.allUsersSubscription = this.managerService.allUsers$.subscribe(users => {
      this.allUsers = users;
    });

    this.userPageSubscription = this.managerService.usersPage$.subscribe(page => {
      this.userPage = page;
      this.userPageArray = new Array<number>();
      for (let i = 1; i <= page; i++) {
        this.userPageArray.push(i);
      }
    });

    this.assignedQuizzesSubscription = this.managerService.userQuizzes$.subscribe(quizzes => {
      this.assignedQuizzes = quizzes;
    });

    this.allQuizzesSubscription = this.managerService.allQuizzes$.subscribe(quizzes => {
      this.allQuizzes = quizzes;
    });

    this.quizPageSubscription = this.managerService.quizzesPage$.subscribe(page => {
      this.quizPage = page;
      this.quizPageArray = new Array<number>();
      for (let i = 1; i <= page; i++) {
        this.quizPageArray.push(i);
      }
    });

    this.managerService.getAllUsers(1);
  }

  setUserPage(page: number) {
    this.userCurrentPage = page;
    this.managerService.getAllUsers(page);
  }

  clickUser(user: AppUser) {
    this.currentUser = user;
    this.managerService.getUserQuizzes(user.userid);
    this.managerService.getAllQuizzes(1);
  }

  resetCurrentUser() {
    this.currentUser = null;
  }

  setQuizPage(page: number) {
    this.quizCurrentPage = page;
    console.log(page);
    this.managerService.getAllQuizzes(page);
  }


}
