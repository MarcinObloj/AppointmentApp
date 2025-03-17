import { Component } from '@angular/core';
import { InputComponent } from '../../shared/input/input.component';

import {
  
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormControl,
  FormsModule
} from '@angular/forms';
@Component({
  selector: 'app-login',
  imports: [InputComponent, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm!: FormGroup;

  ngOnInit() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
      ]),
    });
  }
  onSubmit(): void {
    if (this.loginForm.valid) {
      console.log('logowanie');
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
}
