import { Component, inject } from '@angular/core';
import { InputComponent } from '../../shared/input/input.component';

import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormControl,
  FormsModule,
} from '@angular/forms';
import { AuthService } from '../auth.service';
@Component({
  selector: 'app-login',
  imports: [InputComponent, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm!: FormGroup;
  authService = inject(AuthService);
  fb = inject(FormBuilder);
  errorMessage: string = '';
  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }
  onSubmit(): void {
    if (this.loginForm.valid) {
      this.authService
        .login(this.loginForm.value.username, this.loginForm.value.password)
        .subscribe({
          next: (response) => {
            console.log('Zalogowano:', response);
          },
          error: (error) => {
            console.error('Błąd logowania:', error);
            this.errorMessage = error.message;
          },
        });
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
}
