import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { environment } from '../models/environment.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private HttpClient = inject(HttpClient);

  login(email: string, password: string): Observable<any> {
    const loginData = { email, password };
    return this.HttpClient.post(`${this.apiUrl}/login`, loginData, {
      responseType: 'text',
    }).pipe(
      tap((token) => {
        localStorage.setItem('token', token);
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
}
