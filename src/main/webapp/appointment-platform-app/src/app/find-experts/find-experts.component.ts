// find-experts.component.ts
import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  AbstractControl,
  FormControl,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

import { MatFormFieldModule } from '@angular/material/form-field';

import { CityAutocompleteComponent } from '../shared/city-autocomplete/city-autocomplete.component';
import { Specialization } from '../shared/modal/specialization.model';
import { RegisterService } from '../auth/register/register.service';
import { Router, RouterLink } from '@angular/router';
import { ButtonPrimaryComponent } from '../shared/button-primary/button-primary.component';

@Component({
  selector: 'app-find-experts',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    CityAutocompleteComponent,
    RouterLink,
    ButtonPrimaryComponent,
  ],
  templateUrl: './find-experts.component.html',
  styleUrls: ['./find-experts.component.css'],
})
export class FindExpertsComponent {
  searchForm: FormGroup;
  specializationsList!: Specialization[];
  registerService = inject(RegisterService);
  router = inject(Router);
  constructor(private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      specialization: [''],
      city: [''],
    });
  }
  ngOnInit() {
    this.registerService.getAllSpecializations().subscribe((data) => {
      this.specializationsList = data;
      console.log('Loaded specializations:', this.specializationsList);
    });
  }
  getControl(name: string): FormControl {
    return this.searchForm.get(name) as FormControl;
  }
  onCitySelected(city: string) {
    console.log('Wybrano miasto:', city);
    
  }
  onSearch() {
    if (this.searchForm.valid) {
      console.log('Search data:', this.searchForm.value);
    }
  }

}
