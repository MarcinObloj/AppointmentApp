import { Component, inject, OnInit } from '@angular/core';
import { InputComponent } from "../shared/input/input.component";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ContactService, ContactDTO } from './contact.service';
import { ModalComponent } from '../shared/modal/modal.component';

@Component({
  selector: 'app-contact-page',
  imports: [InputComponent, ReactiveFormsModule,ModalComponent],
  templateUrl: './contact-page.component.html',
  styleUrl: './contact-page.component.css'
})
export class ContactPageComponent implements OnInit {
  contactForm!: FormGroup;

  // Właściwości do obsługi modala
  modalVisible: boolean = false;
  modalMessage: string = '';
  modalIsError: boolean = false;

  contactService = inject(ContactService);

  ngOnInit(): void {
    this.contactForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      phone: new FormControl('', Validators.required),
      message: new FormControl('', Validators.required)
    });
  }

 
  onModalClose(): void {
    this.modalVisible = false;
  }

  onSubmit(): void {
    if (this.contactForm.valid) {
      const contactData: ContactDTO = this.contactForm.value;
      this.contactService.sendContact(contactData).subscribe({
        next: response => {
          console.log('Wiadomość wysłana:', response);
          this.modalMessage = 'Wiadomość została wysłana pomyślnie.';
          this.modalIsError = false;
          this.modalVisible = true;
          this.contactForm.reset();
        },
        error: err => {
          console.error('Błąd przy wysyłaniu wiadomości:', err);
          this.modalMessage = 'Wystąpił błąd przy wysyłaniu wiadomości. Spróbuj ponownie później.';
          this.modalIsError = true;
          this.modalVisible = true;
        }
      });
    } else {
      this.contactForm.markAllAsTouched();
    }
  }
}