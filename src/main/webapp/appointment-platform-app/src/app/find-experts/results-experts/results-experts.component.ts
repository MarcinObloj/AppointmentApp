import { Component, inject } from '@angular/core';
import { FormControl, FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  DateAdapter,
  MAT_DATE_LOCALE,
  MatNativeDateModule,
} from '@angular/material/core';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CityAutocompleteComponent } from '../../shared/city-autocomplete/city-autocomplete.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ExpertProfile } from '../../models/expert.model';
import { ResultsService } from './results.service';
import { Specialization } from '../../shared/modal/specialization.model';
import { RegisterService } from '../../auth/register/register.service';
@Component({
  selector: 'app-results-experts',
  imports: [
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatLabel,
    MatInputModule,

    CityAutocompleteComponent,
  ],
  templateUrl: './results-experts.component.html',
  styleUrl: './results-experts.component.css',
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'pl-PL' }],
})
export class ResultsExpertsComponent {
  showFilters = false;
  filters = { location: '', specialization: '' };
  initialHours = ['10:00', '12:00', '14:00', '16:00'];
  availableHours = [...this.initialHours];
  showHours = false;
  locationControl = new FormControl('');
  selectedDate: Date | null = null;
  showCalendar = false;
  route = inject(ActivatedRoute);
  minDate: Date = new Date();
  specialization: string = '';
  city: string = '';
  experts: ExpertProfile[] = [];
  resultsService = inject(ResultsService);
  registerService = inject(RegisterService);
  specializationsList: Specialization[] = [];
  router = inject(Router);
  constructor(private dateAdapter: DateAdapter<Date>) {
    this.dateAdapter.setLocale('pl-PL');
  }
  ngOnInit(): void {
    // Przechwytywanie parametrów z URL
    this.route.queryParams.subscribe((params) => {
      this.specialization = params['specialization'];
      this.city = params['city'];
      console.log('Specialization:', this.specialization);
      console.log('City:', this.city);
    });
    this.registerService.getAllSpecializations().subscribe((data) => {
      this.specializationsList = data;
      console.log('Loaded specializations:', this.specializationsList);
    });
    this.loadExperts();
  }
  onCitySelected(city: string) {
    this.filters.location = city;
    console.log('Wybrane miasto:', city);
  }
  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  applyFilters() {
    this.city = this.filters.location || this.city;
    this.specialization = this.filters.specialization || this.specialization;

    this.router.navigate([], {
      queryParams: {
        city: this.city,
        specialization: this.specialization,
      },
      queryParamsHandling: 'merge',
    });
    this.loadExperts();
  }
  loadExperts() {
    this.resultsService
      .getExperts(this.city, this.specialization)
      .subscribe((data) => {
        this.experts = data;
      });
  }
  book(hour: string) {
    alert(`Zarezerwowano spotkanie na ${hour}`);
  }

  showMoreHours() {
    const newHours = ['18:00', '20:00'];
    this.showHours = !this.showHours;

    this.availableHours = [...new Set([...this.availableHours, ...newHours])];
  }

  showLessHours() {
    this.availableHours = [...this.initialHours];
    this.showHours = !this.showHours;
  }

  toggleCalendar() {
    this.showCalendar = !this.showCalendar;
  }
}
