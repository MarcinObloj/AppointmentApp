import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { InputComponent } from '../../shared/input/input.component';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, InputComponent, ModalComponent],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  registerForm!: FormGroup;
  message = signal('');
  isError = signal(false);
  router = inject(Router);
  visible = false;
  authService = inject(AuthService);
  constructor(private cdr: ChangeDetectorRef) {}
  ngOnInit(): void {
    this.registerForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
      ]),
      role: new FormControl('', Validators.required),
    });
  }

  closeModal() {
    this.visible = false;
    this.cdr.detectChanges(); 
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          console.log('Zarejestrowano:', response);
          this.message.set(
            'Zarejestrowano poprawnie! Aby aktywować konto wejdz w link aktywacyjny w twoim mailu!'
          );
          this.isError.set(true);
          this.registerForm.reset();
          this.visible = true;
        },
        error: (error) => {
          console.error('Błąd rejestracji:', error);
          this.message.set(
            'Wystąpił błąd podczas rejestracji. Spróbuj ponownie.'
          );
          this.isError.set(false);
          this.visible = true;
        },
      });
    } else {
      this.registerForm.markAllAsTouched();
      this.message.set('Wypełnij wszystkie pola formularza!');
      this.visible = true;
      this.isError.set(true);
    }
  }
}
