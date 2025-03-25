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
import { Specialization } from '../../shared/modal/specialization.model';
import { RegisterService } from './register.service';

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
  selectedFile: File | null = null;
  registerService = inject(RegisterService);
  authService = inject(AuthService);
  specializationsList: Specialization[] = [];
  constructor(private cdr: ChangeDetectorRef) {}
  selectedFilePreview: string | null = null;
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
      specializations: new FormControl([]),
      experienceYears: new FormControl(null),
      description: new FormControl(''),
      photo: new FormControl(null, Validators.required),
    });

    this.registerForm.get('role')?.valueChanges.subscribe((role) => {
      if (role === 'EXPERT') {
        this.registerForm
          .get('experienceYears')
          ?.setValidators(Validators.required);
      } else {
        this.registerForm.get('description')?.clearValidators();
        this.registerForm.get('experienceYears')?.clearValidators();
      }
      this.registerForm.get('description')?.updateValueAndValidity();
      this.registerForm.get('experienceYears')?.updateValueAndValidity();
    });
    this.registerService.getAllSpecializations().subscribe((data) => {
      this.specializationsList = data;
      console.log('Loaded specializations:', this.specializationsList);
    });
  }
  removeFile(){
    this.selectedFile = null;
    this.selectedFilePreview = null;
  }
  onFileChange(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files.length > 0) {
      this.selectedFile = fileInput.files[0];
  
    
      const reader = new FileReader();
      reader.onload = (e) => {
        this.selectedFilePreview = e.target?.result as string;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }
  closeModal() {
    this.visible = false;
    this.cdr.detectChanges();
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const formData = new FormData();

      // Dodajemy wartości z formularza do FormData
      Object.keys(this.registerForm.controls).forEach((key) => {
        const value = this.registerForm.get(key)?.value;

        if (value !== null && value !== undefined && value !== '') {
          if (Array.isArray(value)) {
            value.forEach((item) => {
              formData.append(`${key}[]`, item.toString()); // Upewniamy się, że wartości tablicowe są stringami
            });
          } else if (key !== 'photo') {
            formData.append(key, value.toString());
          }
        }
      });

      // Obsługa zdjęcia - upewniamy się, że wysyłamy plik, a nie stringową ścieżkę
      if (this.selectedFile) {
        formData.append('photo', this.selectedFile, this.selectedFile.name);
      }

      // Wysyłanie formularza do serwisu AuthService
      this.authService.register(formData).subscribe({
        next: (response) => {
          console.log('Zarejestrowano:', response);
          this.message.set(
            'Zarejestrowano poprawnie! Aby aktywować konto, wejdź w link aktywacyjny w twoim mailu!'
          );
          this.isError.set(false);
          this.registerForm.reset();
          this.selectedFile = null; // Resetujemy wybrany plik
          this.visible = true;
        },
        error: (error) => {
          console.error('Błąd rejestracji:', error);
          this.message.set(
            'Wystąpił błąd podczas rejestracji. Spróbuj ponownie.'
          );
          this.isError.set(true);
          this.visible = true;
        },
      });
    } else {
      this.registerForm.markAllAsTouched();
      this.message.set('Wypełnij wszystkie wymagane pola formularza!');
      this.visible = true;
      this.isError.set(true);
    }
  }
}
