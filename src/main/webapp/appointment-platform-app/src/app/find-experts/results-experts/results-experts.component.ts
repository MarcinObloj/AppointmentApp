import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  DateAdapter,
  MAT_DATE_LOCALE,
  MatNativeDateModule,
} from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
@Component({
  selector: 'app-results-experts',
  imports: [
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './results-experts.component.html',
  styleUrl: './results-experts.component.css',
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'pl-PL' }],
})
export class ResultsExpertsComponent {
  showFilters = false;
  filters = { location: '', maxPrice: null };
  initialHours = ['10:00', '12:00', '14:00', '16:00'];
  availableHours = [...this.initialHours];
  showHours = false;
  selectedDate: Date | null = null;
  showCalendar = false;
  minDate: Date = new Date();
  constructor(private dateAdapter: DateAdapter<Date>) {
    this.dateAdapter.setLocale('pl-PL');
  }
  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  applyFilters() {
    console.log('Zastosowano filtry:', this.filters);
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
