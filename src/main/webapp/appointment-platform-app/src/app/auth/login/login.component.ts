import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { InputComponent } from '../../shared/input/input.component';

import { NgClass } from '@angular/common';

import { AuthService } from '../auth.service';

import {
  FormBuilder,
  FormGroup,
  Validators,
  NgControlStatus,
  ReactiveFormsModule,
} from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from '../../shared/modal/modal.component';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [InputComponent, ReactiveFormsModule, ModalComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm!: FormGroup;
  authService = inject(AuthService);
  fb = inject(FormBuilder);
  router = inject(Router);
  message = signal('');
  isError = signal(false);
  visible = false;
  constructor(private cdr: ChangeDetectorRef) {}
  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }
  closeModal() {
    this.visible = false;
    this.cdr.detectChanges();
  }
  onSubmit(): void {
    if (this.loginForm.valid) {
      console.log("🟢 Wysyłane dane:", this.loginForm.value);
  
      this.authService
        .login(this.loginForm.value.username, this.loginForm.value.password)
        .subscribe({
          next: (response) => {
            console.log("✅ Zalogowano poprawnie:", response);
            this.visible = true;
            this.message.set('Zalogowano poprawnie!');
  
            this.isError.set(false);
            this.loginForm.reset();
  
            setTimeout(() => {
              this.router.navigate(['/']);
            }, 5000);
          },
          error: (error) => {
            console.error("🔴 Błąd logowania:", error);
            this.visible = true;
            this.message.set('Niepoprawny email lub hasło');
            this.isError.set(true);
            this.loginForm.reset();
          },
        });
    } else {
      console.log("🔴 Formularz jest niepoprawny:", this.loginForm.errors);
      this.message.set('Niepoprawne dane logowania');
      this.isError.set(true);
      this.visible = true;
      this.loginForm.markAllAsTouched();
    }
  }
  
}
