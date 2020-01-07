import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ManagerMainComponent } from './components/manager-main/manager-main.component';
import { ManagerCreateQuestionComponent } from './components/manager-create-question/manager-create-question.component';
import { ManagerCreateQuizComponent } from './components/manager-create-quiz/manager-create-quiz.component';
import { ManagerAssignUserComponent } from './components/manager-assign-user/manager-assign-user.component';
import { ManagerAssignQuestionComponent } from './components/manager-assign-question/manager-assign-question.component';

const routes: Routes = [
  {
    path: '',
    component: ManagerMainComponent
  },
  {
    path: 'question',
    component: ManagerCreateQuestionComponent
  },
  {
    path: 'quiz',
    component: ManagerCreateQuizComponent
  },
  {
    path: 'assign/user',
    component: ManagerAssignUserComponent
  },
  {
    path: 'assign/question',
    component: ManagerAssignQuestionComponent
  },

];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class ManagerPageRoutingModule { }
