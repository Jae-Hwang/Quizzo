import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Question } from 'src/app/models/question.model';
import { ManagerService } from 'src/app/services/manager.service';

@Component({
  selector: 'app-assign-question-select',
  templateUrl: './assign-question-select.component.html',
  styleUrls: ['./assign-question-select.component.css']
})
export class AssignQuestionSelectComponent implements OnInit, OnChanges {

  // tslint:disable-next-line: no-input-rename
  @Input('i-question')
  question: Question;

  // tslint:disable-next-line: no-input-rename
  @Input('i-currentQuizId')
  currentQuizId: number;

  // tslint:disable-next-line: no-input-rename
  @Input('i-assignedQuestions')
  assignedQuestions: Question[];

  assigned: boolean;

  questionModal: string;
  questionModalTarget: string;


  constructor(private managerService: ManagerService) { }

  ngOnInit() {
  }

  assignQuestion(quid: number, qid: number) {
    console.log(`Question id: ${quid}, Quiz id: ${qid}`);
    this.managerService.assignQuestionToQuiz(quid, qid);
  }


  isAssigned(question: Question, quizQuestions: Question[]): boolean {
    let result = false;
    quizQuestions.forEach(quizQuestion => {
      if (quizQuestion.quid === question.quid) {
        result = true;
      }
    });
    return result;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.question || changes.assignedQuestions) {
      this.assigned = this.isAssigned(this.question, this.assignedQuestions);
      this.questionModal = `questionModal${this.question.quid}`;
      this.questionModalTarget = `#questionModal${this.question.quid}`;
    }
  }
}
