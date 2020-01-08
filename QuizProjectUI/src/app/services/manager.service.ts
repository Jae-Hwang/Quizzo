import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Question } from '../models/question.model';
import { Quiz } from '../models/quiz.model';
import { ReplaySubject } from 'rxjs';
import { AppUser } from '../models/app.user.model';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  private allUsersStream = new ReplaySubject<AppUser[]>(1);
  allUsers$ = this.allUsersStream.asObservable();
  usersMap = new Map<number, AppUser[]>();

  private usersPageStream = new ReplaySubject<number>(1);
  usersPage$ = this.usersPageStream.asObservable();

  private allQuizzesStream = new ReplaySubject<Quiz[]>(1);
  allQuizzes$ = this.allQuizzesStream.asObservable();
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

  private userQuizzesStream = new ReplaySubject<Quiz[]>(1);
  userQuizzes$ = this.userQuizzesStream.asObservable();

  private API_URL = 'http://localhost:8080/QuizProject';

  constructor(private httpClient: HttpClient) { }

  getAllUsers(page: number) {
    if (this.usersMap.has(page)) {
      console.log(`User page(${page}) from cache.`);
      this.allUsersStream.next(this.usersMap.get(page));
      return;
    }
    this.httpClient.get<any>(`${this.API_URL}/users?page=${page}`, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(
      data => {
        console.log(`User max page: ${data.headers.get('X-page')}`);
        this.allUsersStream.next(data.body);
        this.usersPageStream.next(parseInt(data.headers.get('X-page'), 0));
        this.usersMap.set(page, data.body);
      },
      err => {
        console.log(err);
      }
    );
  }

  getAllQuizzes(page: number) {
    if (this.quizzesMap.has(page)) {
      console.log(`Quiz page(${page}) from cache.`);
      this.allQuizzesStream.next(this.quizzesMap.get(page));
      return;
    }
    this.httpClient.get<any>(`${this.API_URL}/quizzes?page=${page}`, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(
      data => {
        console.log(`Quiz max page: ${data.headers.get('X-page')}`);
        this.allQuizzesStream.next(data.body);
        this.quizzesPageStream.next(parseInt(data.headers.get('X-page'), 0));
        this.quizzesMap.set(page, data.body);
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
        console.log(`Question max page: ${data.headers.get('X-page')}`);
        this.allQuestionsStream.next(data.body);
        this.questionsPageStream.next(parseInt(data.headers.get('X-page'), 0));
        this.questionsMap.set(page, data.body);
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
        console.log(`Retrieved Questions under qid: ${qid}`);
        this.quizQuestionsStream.next(data);
      },
      err => {
        console.log(err);
      }
    );
  }

  getUserQuizzes(userid: number) {
    this.httpClient.get<Quiz[]>(`${this.API_URL}/quizzes?userid=${userid}`).subscribe(
      data => {
        // data.forEach(el => console.log(el));
        console.log(`Retrieved Quizzes under userid: ${userid}`);
        this.userQuizzesStream.next(data);
      },
      err => {
        console.log(err);
      }
    );
  }

  assignQuestionToQuiz(quid: number, qid: number) {
    this.httpClient.post(`${this.API_URL}/quizzes?quid=${quid}&qid=${qid}`, 0).subscribe(
      data => {
        console.log(`Question:${quid} assigned to Quiz:${qid}`);
        this.getQuizQuestions(qid);
      },
      err => {
        console.log(err);
      }
    );
  }

  assignQuizToUser(qid: number, userid: number) {
    this.httpClient.post(`${this.API_URL}/quizzes?userid=${userid}&qid=${qid}`, 0).subscribe(
      data => {
        console.log(`Quiz:${qid} assigned to User:${userid}`);
        this.getUserQuizzes(userid);
      },
      err => {
        console.log(err);
      }
    );
  }
}
