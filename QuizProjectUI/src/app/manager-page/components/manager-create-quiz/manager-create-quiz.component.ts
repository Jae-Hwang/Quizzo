import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { AppUser } from 'src/app/models/app.user.model';
import { Subscription } from 'rxjs';
import { ManagerService } from 'src/app/services/manager.service';
import { Quiz } from 'src/app/models/quiz.model';

@Component({
  selector: 'app-manager-create-quiz',
  templateUrl: './manager-create-quiz.component.html',
  styleUrls: ['./manager-create-quiz.component.css']
})
export class ManagerCreateQuizComponent implements OnInit {

  currentUser: AppUser;
  currentUserSubscription: Subscription;

  quizName = '';

  constructor(private authService: AuthService, private managerService: ManagerService) { }

  ngOnInit() {
    this.currentUserSubscription = this.authService.$currentUser.subscribe(user => {
      this.currentUser = user;
    });
  }

  submit() {
    this.managerService.createQuiz(new Quiz(0, this.quizName, 0, this.currentUser.userid));
  }
}
