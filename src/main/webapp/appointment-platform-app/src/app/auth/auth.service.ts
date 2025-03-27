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
import { Expert } from '../models/expert.model';
import { LoginResponse } from '../models/loginResponse.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private HttpClient = inject(HttpClient);
  private $isLoggedIn = new BehaviorSubject<boolean>(this.hasToken());
  expertData?: LoginResponse;
  private hasToken(): boolean {
    return !!sessionStorage.getItem('token');
  }
  login(email: string, password: string): Observable<any> {
    const loginData = { email, password };
return this.HttpClient.post<LoginResponse>(`${this.apiUrl}/login`, loginData).pipe(
  tap((response) => {
    sessionStorage.setItem('token', response.token);
    sessionStorage.setItem('role', response.role);
    if(response.role === 'EXPERT') {
      sessionStorage.setItem('expert', JSON.stringify(response));
    }
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
  register(registerData: FormData): Observable<any> {
    return this.HttpClient.post(`${this.apiUrl}/register`, registerData).pipe(
      catchError((error) => {
        return throwError(() => new Error('Wystąpił błąd podczas rejestracji'));
      })
    );
  }

  logout(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('role');
    sessionStorage.removeItem('expert');
    this.$isLoggedIn.next(false);
  }
  setLoggedIn() {
    this.$isLoggedIn.next(true);
  }

  get isLoggedIn(): Observable<boolean> {
    return this.$isLoggedIn.asObservable();
  }
}
