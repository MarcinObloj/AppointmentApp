import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DateAdapter, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ButtonPrimaryComponent } from "../shared/button-primary/button-primary.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  imports: [
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    ButtonPrimaryComponent,
    CommonModule
],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'pl-PL' }],

})
export class ProfileComponent {
  showFilters = false;
  filters = { location: '', maxPrice: null };
  initialHours = ['10:00', '12:00', '14:00', '16:00'];
  availableHours = [...this.initialHours];
  showHours = false;
  selectedDate: Date | null = null;
  showCalendar = false;
  minDate: Date = new Date();
  filters1 = ["Wszystkie opinie (64)", "Pozytywne (64)", "Neutralne (0)", "Negatywne (0)"];
  activeFilter = this.filters1[0];

  // Sortowanie opinii
  sortOptions = ["Najnowsza", "Najwyższa", "Najniższa"];
  activeSort = this.sortOptions[0];

  // Czy opinia jest przydatna?
  isHelpful: boolean | null = null;

  // Zmiana aktywnego filtra
  setActiveFilter(filter: string) {
    this.activeFilter = filter;
  }

  // Zmiana aktywnego sortowania
  setActiveSort(sort: string) {
    this.activeSort = sort;
  }

  // Kliknięcie "Przydatne" lub "Nieprzydatne"
  toggleHelpful(value: boolean) {
    this.isHelpful = this.isHelpful === value ? null : value;
  }
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
    console.log(`Zarezerwowano godzinę: ${hour}`);
  }

  showMoreHours() {
    const newHours = ['18:00', '20:00'];
    this.showHours = true;
    this.availableHours = [...new Set([...this.initialHours, ...newHours])];
  }

  showLessHours() {
    this.showHours = false;
    this.availableHours = [...this.initialHours];
  }

  toggleCalendar() {
    this.showCalendar = !this.showCalendar;
  }
}
