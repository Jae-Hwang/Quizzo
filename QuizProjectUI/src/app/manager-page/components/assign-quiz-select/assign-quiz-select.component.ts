import { Component, OnInit, OnChanges, Input, SimpleChanges } from '@angular/core';
import { Quiz } from 'src/app/models/quiz.model';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-assign-quiz-select',
  templateUrl: './assign-quiz-select.component.html',
  styleUrls: ['./assign-quiz-select.component.css']
})
export class AssignQuizSelectComponent implements OnInit, OnChanges {

  // tslint:disable-next-line: no-input-rename
  @Input('i-quiz')
  quiz: Quiz;

  // tslint:disable-next-line: no-input-rename
  @Input('i-currentUserId')
  currentUserId: number;

  // tslint:disable-next-line: no-input-rename
  @Input('i-assignedQuizzes')
  assignedQuizzes: Quiz[];

  assigned: boolean;

  quizModal: string;
  quizModalTarget: string;


  constructor(private managerService: ManagerService) { }

  ngOnInit() {
  }

  assignQuiz(qid: number, userid: number) {
    console.log(`Quiz id: ${qid}, User id: ${userid}`);
    this.managerService.assignQuizToUser(qid, userid);
  }


  isAssigned(quiz: Quiz, assignedQuizzes: Quiz[]): boolean {
    let result = false;
    assignedQuizzes.forEach(assignedQuiz => {
      if (assignedQuiz.qid === quiz.qid) {
        result = true;
      }
    });
    return result;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.quiz || changes.assignedQuizzes) {
      this.assigned = this.isAssigned(this.quiz, this.assignedQuizzes);
      this.quizModal = `questionModal${this.quiz.qid}`;
      this.quizModalTarget = `#questionModal${this.quiz.qid}`;
    }
  }
}
