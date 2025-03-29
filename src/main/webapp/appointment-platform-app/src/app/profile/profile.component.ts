import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  DateAdapter,
  MAT_DATE_LOCALE,
  MatNativeDateModule,
} from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ButtonPrimaryComponent } from '../shared/button-primary/button-primary.component';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { UserService } from './user.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  photoUrl: string;
}

export interface Service {
  id: number;
  name: string;
  price: number;
}

export interface ExpertProfile {
  id: number;
  description: string;
  experienceYears: number;
  city: string;
  street: string;
  clientTypes: string[];
  ageGroups: string[];
  services: Service[];
  user: User;
}

@Component({
  selector: 'app-profile',
  imports: [
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    ButtonPrimaryComponent,
    CommonModule,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'pl-PL' }],
})
export class ProfileComponent implements OnInit {
  showFilters = false;
  filters = { location: '', maxPrice: null };
  initialHours = ['10:00', '12:00', '14:00', '16:00'];
  availableHours = [...this.initialHours];
  showHours = false;
  selectedDate: Date | null = null;
  showCalendar = false;
  minDate: Date = new Date();
  filters1 = [
    'Wszystkie opinie (64)',
    'Pozytywne (64)',
    'Neutralne (0)',
    'Negatywne (0)',
  ];
  activeFilter = this.filters1[0];
  expertProfile!: ExpertProfile;
  route = inject(ActivatedRoute);
  userService = inject(UserService);
  sortOptions = ['Najnowsza', 'Najwyższa', 'Najniższa'];
  activeSort = this.sortOptions[0];
  mapUrl: SafeResourceUrl | null = null;
  selectedServiceId: number = 1;
  selectedService: Service | null = null;
  sanitizer = inject(DomSanitizer);
  isHelpful: boolean | null = null;
  isClientTypesExpanded = false;
  isAgeGroupsExpanded = false;
  isReviewExpanded = false;
  visibleServices = 5;
  isExpanded = false; // Dodaj tę zmienną
  ngOnInit(): void {
    const expertId = this.route.snapshot.params['id'];
    this.userService.getExpertById(expertId).subscribe({
      next: (expertProfile) => {
        this.expertProfile = expertProfile;
        console.log(expertProfile);
        console.log(expertProfile.description.length);
        this.selectedService = this.expertProfile.services[0] || null;
        this.selectedServiceId = this.selectedService?.id || 0;
      },
      error: (error) => {
        console.error('Błąd pobierania profilu:', error);
      },
    });
    this.mapUrl = this.getGoogleMapsUrl(
      this.expertProfile.city,
      this.expertProfile.street
    );
  }
  onServiceChange() {
    this.selectedService =
      this.expertProfile.services.find(
        (service) => service.id === this.selectedServiceId
      ) || null;
  }
  getGoogleMapsUrl(city: string, street: string): SafeResourceUrl {
    const apiKey = 'AIzaSyAOVYRIgupAurZup5y1PRh8Ismb1A3lLao';
    const query = encodeURIComponent(`${city}, ${street}`);
    const url = `https://www.google.com/maps/embed/v1/place?key=${apiKey}&q=${query}`;

    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  setActiveFilter(filter: string) {
    this.activeFilter = filter;
  }

  setActiveSort(sort: string) {
    this.activeSort = sort;
  }

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
