import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  catchError,
  map,
  Observable,
  tap,
  throwError,
} from 'rxjs';
import { environment } from '../models/environment.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private HttpClient = inject(HttpClient);
  private $isLoggedIn = new BehaviorSubject<boolean>(this.hasToken());

  private hasToken(): boolean {
    return !!sessionStorage.getItem('token');
  }
  login(email: string, password: string): Observable<any> {
    const loginData = { email, password };
    return this.HttpClient.post(`${this.apiUrl}/login`, loginData, {
      responseType: 'text',
    }).pipe(
      tap((token) => {
        sessionStorage.setItem('token', token);
        this.$isLoggedIn.next(true);
      }),
      catchError((error) => {
        if (error.status === 401) {
          return throwError(() => new Error('Niepoprawny email lub hasło'));
        } else {
          return throwError(() => new Error('Wystąpił błąd serwera'));
        }
      })
    );
  }
  register(registerData: User):Observable<User> {
    return this.HttpClient.post<User>(`${this.apiUrl}/register`, registerData);
  }
  logout(): void {
    sessionStorage.removeItem('token');
    this.$isLoggedIn.next(false);
  }
  setLoggedIn() {
    this.$isLoggedIn.next(true);
  }
  get isLoggedIn(): Observable<boolean> {
    return this.$isLoggedIn.asObservable();
  }
}
