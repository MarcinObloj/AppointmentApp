import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Specialization } from '../../shared/modal/specialization.model';
import { environment } from '../../models/environment.model';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  apiUrl= environment.apiUrl;
  http= inject(HttpClient);
  getAllSpecializations(): Observable<Specialization[]> {
    return this.http.get<Specialization[]>(`${this.apiUrl}/specializations`);
  }
}
