import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { Quiz } from '../models/quiz.model';
import { HttpClient } from '@angular/common/http';
import { Question } from '../models/question.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private API_URL = 'http://localhost:8080/QuizProject';

  private userQuizzesStream = new ReplaySubject<Quiz[]>(1);
  userQuizzes$ = this.userQuizzesStream.asObservable();

  private currentQuizStream = new ReplaySubject<Quiz>(1);
  currentQuiz$ = this.currentQuizStream.asObservable();

  private questionsStream = new ReplaySubject<Question[]>(1);
  questions$ = this.questionsStream.asObservable();

  constructor(private httpClient: HttpClient) { }

  getUserQuizzes(userid: number) {
    this.httpClient.get<Quiz[]>(`${this.API_URL}/quizzes?userid=${userid}`).subscribe(
      data => {
        console.log(`Retrieved Quizzes under userid: ${userid}`);
        this.userQuizzesStream.next(data);
      },
      err => {
        console.log(err);
      }
    );
  }

  setCurrentQuiz(quiz: Quiz) {
    this.currentQuizStream.next(quiz);
  }

  getQuestions(qid: number) {
    this.httpClient.get<Question[]>(`${this.API_URL}/questions?qid=${qid}`).subscribe(
      data => {
        data.forEach(ele => console.log(ele));
        console.log(`Retrieved Questions under qid: ${qid}`);
        this.questionsStream.next(data);
      },
      err => {
        console.log(err);
      }
    );
  }

  /* tbi
  getUserGroups(userid: number) {
    this.httpClient.get<Quiz[]>(`${this.API_URL}/groups?userid=${userid}`, {
      withCredentials: true
    }).subscribe(
      data => {
        console.log(`Retrieved groups under userid: ${userid}`);
        this.userQuizzesStream.next(data);
      },
      err => {
        console.log(err);
      }
    );
  }
  */
}
