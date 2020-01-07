import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserMainComponent } from './components/user-main/user-main.component';
import { UserTakingQuizComponent } from './components/user-taking-quiz/user-taking-quiz.component';

const routes: Routes = [
  {
    path: '',
    component: UserMainComponent
  },
  {
    path: 'quiz',
    component: UserTakingQuizComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserPageRoutingModule { }
