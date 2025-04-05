import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  catchError,
  map,
  Observable,
  of,
  tap,
  throwError,
} from 'rxjs';
import { environment } from '../models/environment.model';
import { User } from '../models/user.model';

import { LoginResponse } from '../models/loginResponse.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private HttpClient = inject(HttpClient);
  private $isLoggedIn = new BehaviorSubject<boolean>(false);
  expertData?: LoginResponse;
  checkAuthState(): Observable<boolean> {
    return this.getCurrentUser().pipe(
      map(user => !!user),
      catchError(() => of(false)),
      tap(isLoggedIn => this.$isLoggedIn.next(isLoggedIn))
    );
  }
  login(email: string, password: string): Observable<LoginResponse> {
    const loginData = { email, password };
    return this.HttpClient.post<LoginResponse>(
      `${this.apiUrl}/login`,
      loginData,
      { withCredentials: true } 
    ).pipe(
      tap((response) => {
        
        if (response.role === 'EXPERT') {
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
  getClientId(): Observable<number | null> {
    return this.getCurrentUser().pipe(
      map(user => user.id),
      catchError(() => of(null))
    );
  }
  
  register(registerData: FormData): Observable<any> {
    return this.HttpClient.post(`${this.apiUrl}/register`, registerData).pipe(
      catchError((error) => {
        return throwError(() => new Error('Wystąpił błąd podczas rejestracji'));
      })
    );
  }
  getCurrentUser(): Observable<User> {
    return this.HttpClient.get<User>(`${this.apiUrl}/current-user`, 
      { withCredentials: true }
    );
  }
  logout(): Observable<void> {
    return this.HttpClient.post<void>(`${this.apiUrl}/logout`, null, 
      { withCredentials: true }
    ).pipe(
      tap(() => {
        sessionStorage.clear();
        this.$isLoggedIn.next(false);
      })
    );
  }
  setLoggedIn() {
    this.$isLoggedIn.next(true);
  }

  get isLoggedIn(): Observable<boolean> {
    return this.$isLoggedIn.asObservable();
  }
}
