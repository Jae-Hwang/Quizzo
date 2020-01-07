import { Injectable } from '@angular/core';
import { ReplaySubject, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AppUser } from '../models/app.user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API_URL = 'http://localhost:8080/QuizProject';

  private currentUserStream = new ReplaySubject<AppUser>(1);
  $currentUser = this.currentUserStream.asObservable();

  private loginErrorStream = new Subject<string>();
  $loginError = this.loginErrorStream.asObservable();

  constructor(private httpClient: HttpClient, private router: Router) {
    this.httpClient.get<AppUser>(`${this.API_URL}/auth/session-user`, {
      withCredentials: true
    }).subscribe(
      data => {
        if (data === null) {
          console.log('not currently logged in');
          this.router.navigateByUrl('/login');
        } else {
          console.log('logged in');
          console.log(data);
          this.currentUserStream.next(data);
        }
      },
      err => {
        console.log('not currently logged in');
      }
    );
  }

  login(credentials) {
    const url = `${this.API_URL}/auth/login?username=${credentials.username}&password=${credentials.password}`;
    this.httpClient.post<AppUser>(url, '', {
      withCredentials: true
    }).subscribe(
      data => {
        console.log('Logged in at Auth Service');
        console.log(data);
        if (data.type) {
          this.router.navigateByUrl('/user');
        } else {
          this.router.navigateByUrl('/manager');
        }
        this.currentUserStream.next(data);
      },
      err => {
        console.log(err);
        this.loginErrorStream.next('Failed to Login');
      }
    );
  }

  logout() {
    this.currentUserStream.next(null);
    this.httpClient.post(`${this.API_URL}/auth/logout`, {
      withCredentials: true
    }).subscribe(
      data => {
        console.log('Logged out successfully');
      },
      err => {
        console.log(err);
      }
    );
  }

}
