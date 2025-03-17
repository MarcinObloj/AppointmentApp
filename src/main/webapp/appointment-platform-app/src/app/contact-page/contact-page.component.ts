import { Component } from '@angular/core';
import { InputComponent } from "../shared/input/input.component";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-contact-page',
  imports: [InputComponent,ReactiveFormsModule],
  templateUrl: './contact-page.component.html',
  styleUrl: './contact-page.component.css'
})
export class ContactPageComponent {
  contactForm!:FormGroup
  ngOnInit(): void {
    this.contactForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      phone: new FormControl('', Validators.required),
      message: new FormControl('', Validators.required)
    });
  }

  onSubmit(): void {
    if (this.contactForm.valid) {
      console.log('Wiadomość wysłana:', this.contactForm.value);
      // ... dalsza logika wysyłki wiadomości ...
    } else {
      this.contactForm.markAllAsTouched();
    }
  }
}
