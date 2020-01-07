import { Component, OnInit, OnDestroy } from '@angular/core';
import { Question } from 'src/app/models/question.model';
import { AuthService } from 'src/app/services/auth.service';
import { AppUser } from 'src/app/models/app.user.model';
import { Subscription } from 'rxjs';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-manager-create-question',
  templateUrl: './manager-create-question.component.html',
  styleUrls: ['./manager-create-question.component.css']
})
export class ManagerCreateQuestionComponent implements OnInit, OnDestroy {

  type = 0;

  desc = '';
  catagory = '';

  selection1 = '';
  selection2 = '';
  selection3 = '';
  selection4 = '';
  selection5 = '';

  quizAnswer = '';

  currentUser: AppUser;
  currentUserSubscription: Subscription;

  constructor(private authService: AuthService, private managerService: ManagerService) { }

  ngOnInit() {
    this.currentUserSubscription = this.authService.$currentUser.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngOnDestroy() {
    this.currentUserSubscription.unsubscribe();
  }

  setType(type: number) {
    console.log(type);
    this.type = type;

    this.selection1 = '';
    this.selection2 = '';
    this.selection3 = '';
    this.selection4 = '';
    this.selection5 = '';
    this.quizAnswer = '';
  }

  submit() {
    const question: Question = new Question(
      0,
      this.desc,
      this.type,
      [this.selection1, this.selection2, this.selection3, this.selection4, this.selection5],
      this.catagory,
      0,
      this.currentUser.userid);

    this.managerService.createQuestion(question, this.quizAnswer);

    this.desc = '';
    this.type = 0;
    this.selection1 = '';
    this.selection2 = '';
    this.selection3 = '';
    this.selection4 = '';
    this.selection5 = '';
    this.quizAnswer = '';
    this.catagory = '';
  }
}
