import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { Quiz } from '../models/quiz.model';
import { HttpClient } from '@angular/common/http';
import { Question } from '../models/question.model';
import { Router } from '@angular/router';
import { Result } from '../models/result.model';

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

  private gradeStream = new ReplaySubject<number>(1);
  grade$ = this.gradeStream.asObservable();

  private resultsStream = new ReplaySubject<Result[]>(1);
  results$ = this.resultsStream.asObservable();

  constructor(private httpClient: HttpClient, private router: Router) { }

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

  getResultsByUserid(userid: number) {
    this.httpClient.get<Result[]>(`${this.API_URL}/results?userid=${userid}`).subscribe(
      data => {
        console.log(`Retrieved Results under userid: ${userid}`);
        this.resultsStream.next(data);
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

  submitQuiz(userid: number, qid: number, answers: string) {
    this.httpClient.post<any>(`${this.API_URL}/results?userid=${userid}&qid=${qid}&answers=${answers}`, 0, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(
      data => {
        console.log(`Grade: ${data.headers.get('X-grade')}`);
        this.gradeStream.next(parseFloat(data.headers.get('X-grade')));
        this.router.navigateByUrl('/user/finished');
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
