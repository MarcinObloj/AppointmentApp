import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
  FormArray,
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
      photo: new FormControl(null),
      city: new FormControl(''),
      street: new FormControl(''),
      ageGroups: new FormControl([]),
      clientTypes: new FormControl([]),
      services: new FormArray([]),
      workingHours: new FormArray([]),
    });

    this.registerForm.get('role')?.valueChanges.subscribe((role) => {
      if (role === 'EXPERT') {
        this.registerForm
          .get('experienceYears')
          ?.setValidators(Validators.required);
        this.registerForm.get('city')?.setValidators(Validators.required);
        this.registerForm.get('street')?.setValidators(Validators.required);
        this.registerForm
          .get('clientTypes')
          ?.setValidators(Validators.required);
        this.registerForm.get('ageGroups')?.setValidators(Validators.required);
        this.registerForm
          .get('services')
          ?.setValidators([Validators.required, Validators.minLength(1)]);
      } else {
        this.registerForm.get('experienceYears')?.clearValidators();
        this.registerForm.get('city')?.clearValidators();
        this.registerForm.get('street')?.clearValidators();
        this.registerForm.get('clientTypes')?.clearValidators();
        this.registerForm.get('ageGroups')?.clearValidators();
        this.registerForm.get('services')?.clearValidators();
      }

      // Aktualizacja stanu walidacji pól
      this.registerForm.get('experienceYears')?.updateValueAndValidity();
      this.registerForm.get('city')?.updateValueAndValidity();
      this.registerForm.get('street')?.updateValueAndValidity();
      this.registerForm.get('clientTypes')?.updateValueAndValidity();
      this.registerForm.get('ageGroups')?.updateValueAndValidity();
      this.registerForm.get('services')?.updateValueAndValidity();
    });

    this.registerService.getAllSpecializations().subscribe((data) => {
      this.specializationsList = data;
      console.log('Loaded specializations:', this.specializationsList);
    });
  }
  get workingHours(): FormArray {
    return this.registerForm.get('workingHours') as FormArray;
  }

  addWorkingHour(): void {
    this.workingHours.push(
      new FormGroup({
        dayOfWeek: new FormControl('', Validators.required),
        startHour: new FormControl('', Validators.required),
        endHour: new FormControl('', Validators.required),
      })
    );
  }

  removeWorkingHour(index: number): void {
    this.workingHours.removeAt(index);
  }
  removeFile() {
    this.selectedFile = null;
    this.selectedFilePreview = null;
  }
  get services(): FormArray {
    return this.registerForm.get('services') as FormArray;
  }
  removeService(index: number): void {
    this.services.removeAt(index);
  }

  addService(): void {
    this.services.push(
      new FormGroup({
        name: new FormControl('', Validators.required),
        price: new FormControl('', [Validators.required, Validators.min(0)]),
      })
    );
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

      Object.keys(this.registerForm.controls).forEach((key) => {
        const value = this.registerForm.get(key)?.value;
        if (key === 'workingHours' && Array.isArray(value)) {
          value.forEach((wh: any, index: number) => {
            formData.append(`workingHours[${index}].dayOfWeek`, wh.dayOfWeek);
            formData.append(`workingHours[${index}].startHour`, wh.startHour);
            formData.append(`workingHours[${index}].endHour`, wh.endHour);
          });
        } else if (key === 'services' && Array.isArray(value)) {
          value.forEach((service: any, index: number) => {
            formData.append(`services[${index}].name`, service.name);
            formData.append(`services[${index}].price`, service.price.toString());
          });
        } else if (value !== null && value !== undefined && value !== '') {
          formData.append(key, value.toString());
        }
      });

      if (this.selectedFile) {
        formData.append('photo', this.selectedFile, this.selectedFile.name);
      }

      this.authService.register(formData).subscribe({
        next: (response) => {
          console.log('Zarejestrowano:', response);
          this.message.set('Zarejestrowano poprawnie! Sprawdź email.');
          this.isError.set(false);
          this.registerForm.reset();
          this.selectedFile = null;
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
