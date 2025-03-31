import { Component, inject, QueryList, ViewChildren } from '@angular/core';
import { FormControl, FormsModule } from '@angular/forms';
import {
  MatDatepicker,
  MatDatepickerModule,
} from '@angular/material/datepicker';
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
import { ExpertCardComponent } from "./expert-card.component";
@Component({
  selector: 'app-results-experts',
  imports: [
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    
    MatInputModule,
    CityAutocompleteComponent,
    ExpertCardComponent
],
  templateUrl: './results-experts.component.html',
  styleUrl: './results-experts.component.css',
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'pl-PL' }],
})
export class ResultsExpertsComponent {
  showFilters = false;
  filters = { location: '', specialization: null };
  locationControl = new FormControl('');
  minDate: Date = new Date();
  specialization: string = '';
  city: string = '';
  experts: ExpertProfile[] = [];
  specializationsList: Specialization[] = [];

  constructor(
    private dateAdapter: DateAdapter<Date>,
    private route: ActivatedRoute,
    private router: Router,
    private resultsService: ResultsService,
    private registerService: RegisterService
  ) {
    this.dateAdapter.setLocale('pl-PL');
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.specialization = params['specialization'];
      this.city = params['city'];
    });
    
    this.registerService.getAllSpecializations().subscribe(data => {
      this.specializationsList = data;
    });
    
    this.loadExperts();
  }

  onCitySelected(city: string) {
    this.filters.location = city;
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
      .subscribe((data: ExpertProfile[]) => {
        this.experts = data;
      });
  }
}