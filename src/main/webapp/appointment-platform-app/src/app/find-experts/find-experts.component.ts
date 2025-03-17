import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-find-experts',
  imports: [ReactiveFormsModule],
  templateUrl: './find-experts.component.html',
  styleUrl: './find-experts.component.css',
})
export class FindExpertsComponent {
  searchForm: FormGroup;
  specializations: string[] = ['Prawnik', 'Lekarz', 'Inżynier', 'Nauczyciel'];
  cities: string[] = ['Warszawa', 'Kraków', 'Wrocław', 'Poznań'];

  constructor(private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      specialization: [''],
      city: [''],
    });
  }

  onSearch() {
    console.log(this.searchForm.value);
  }
}
