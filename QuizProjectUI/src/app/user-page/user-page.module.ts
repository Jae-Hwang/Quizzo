import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserPageRoutingModule } from './user-page-routing.module';

import { UserMainComponent } from './components/user-main/user-main.component';
import { UserQuizzesComponent } from './components/user-quizzes/user-quizzes.component';
import { UserTakingQuizComponent } from './components/user-taking-quiz/user-taking-quiz.component';
import { FormsModule } from '@angular/forms';
import { FinishedGradeComponent } from './components/finished-grade/finished-grade.component';



@NgModule({
  declarations: [
    UserMainComponent,
    UserQuizzesComponent,
    UserTakingQuizComponent,
    FinishedGradeComponent
  ],
  imports: [
    CommonModule,
    UserPageRoutingModule,
    FormsModule
  ]
})
export class UserPageModule { }
