import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Question } from '../models/question.model';
import { Quiz } from '../models/quiz.model';
import { ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  private quizzesStream = new ReplaySubject<Quiz[]>(1);
  quizzes$ = this.quizzesStream.asObservable();
  quizzesMap = new Map<number, Quiz[]>();

  private quizzesPageStream = new ReplaySubject<number>(1);
  quizzesPage$ = this.quizzesPageStream.asObservable();

  private quizQuestionsStream = new ReplaySubject<Question[]>(1);
  quizQuestions$ = this.quizQuestionsStream.asObservable();

  private allQuestionsStream = new ReplaySubject<Question[]>(1);
  allQuestions$ = this.allQuestionsStream.asObservable();
  questionsMap = new Map<number, Question[]>();

  private questionsPageStream = new ReplaySubject<number>(1);
  questionsPage$ = this.questionsPageStream.asObservable();

  private API_URL = 'http://localhost:8080/QuizProject';

  constructor(private httpClient: HttpClient) { }

  getQuizzes(page: number) {
    if (this.quizzesMap.has(page)) {
      console.log(`Quiz page(${page}) from cache.`);
      this.quizzesStream.next(this.quizzesMap.get(page));
      return;
    }
    this.httpClient.get<any>(`${this.API_URL}/quizzes?page=${page}`, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(
      data => {
        console.log(data.headers.get('X-page'));
        this.quizzesStream.next(data.body);
        this.quizzesPageStream.next(parseInt(data.headers.get('X-page'), 0));
      },
      err => {
        console.log(err);
      }
    );
  }

  createQuestion(question: Question, answer: string) {
    this.httpClient.post(`${this.API_URL}/questions?answer=${answer}`, question).subscribe(
      data => {
        console.log('Question Created Successfully');
      },
      err => {
        console.log(err);
      }
    );
  }

  createQuiz(quiz: Quiz) {
    // console.log(`Input: ${quiz.name}, ${quiz.createdBy}`);
    this.httpClient.post(`${this.API_URL}/quizzes`, quiz).subscribe(
      data => {
        console.log('Quiz Created Successfully');
      },
      err => {
        console.log(err);
      }
    );
  }

  getQuizQuestions(qid: number) {
    this.httpClient.get<Question[]>(`${this.API_URL}/questions?qid=${qid}`).subscribe(
      data => {
        data.forEach(ele => console.log(ele));
        console.log(`Retrieved Questions under qid: ${qid}`);
        this.quizQuestionsStream.next(data);
      },
      err => {
        console.log(err);
      }
    );
  }

  getAllQuestions(page: number) {
    if (this.questionsMap.has(page)) {
      console.log(`Question page(${page}) from cache.`);
      this.allQuestionsStream.next(this.questionsMap.get(page));
      return;
    }
    this.httpClient.get<any>(`${this.API_URL}/questions?page=${page}`, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(
      data => {
        console.log(data.headers.get('X-page'));
        this.allQuestionsStream.next(data.body);
        this.questionsPageStream.next(parseInt(data.headers.get('X-page'), 0));
      },
      err => {
        console.log(err);
      }
    );
  }
}
