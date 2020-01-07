import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerMainComponent } from './components/manager-main/manager-main.component';
import { ManagerPageRoutingModule } from './manager-page-routing.module';
import { ManagerCreateQuestionComponent } from './components/manager-create-question/manager-create-question.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { ManagerCreateQuizComponent } from './components/manager-create-quiz/manager-create-quiz.component';
import { ManagerAssignUserComponent } from './components/manager-assign-user/manager-assign-user.component';
import { ManagerAssignQuestionComponent } from './components/manager-assign-question/manager-assign-question.component';



@NgModule({
  declarations: [
    ManagerMainComponent,
    ManagerCreateQuestionComponent,
    ManagerCreateQuizComponent,
    ManagerAssignUserComponent,
    ManagerAssignQuestionComponent
  ],
  imports: [
    CommonModule,
    ManagerPageRoutingModule,
    NgbModule,
    FormsModule
  ]
})
export class ManagerPageModule { }
