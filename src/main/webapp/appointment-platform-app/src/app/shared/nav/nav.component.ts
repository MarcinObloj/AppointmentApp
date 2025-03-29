import { Component, inject, OnInit } from '@angular/core';

import { ButtonPrimaryComponent } from '../button-primary/button-primary.component';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { Specialization } from '../modal/specialization.model';
import { RegisterService } from '../../auth/register/register.service';
import { NgClass } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CitySearchService } from '../../find-experts/city-search.service';
import { CityAutocompleteComponent } from "../city-autocomplete/city-autocomplete.component";

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [ButtonPrimaryComponent, RouterLink, NgClass, ReactiveFormsModule, CityAutocompleteComponent],
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
})
export class NavComponent  implements OnInit {
  isOpen = false;
  router = inject(Router);
  authService = inject(AuthService);
  isLoggedIn = false;
  
  specializationsList:Specialization[]= [];
  registerService= inject(RegisterService);
  cityControl = new FormControl();
  filteredCities: string[] = [];
  isLoading = false;
  showDropdown = false;
  selectedCity: string | null = null;
  citySearchService = inject(CitySearchService);
  onCitySelected(city: string) {
    console.log('Wybrano miasto:', city);
    // Tutaj możesz dodać dodatkową logikę
  }
  toggleMenu() {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      document.body.classList.add('overflow-hidden');
    } else {
      document.body.classList.remove('overflow-hidden');
    }
  }

  ngOnInit(): void {
    this.authService.isLoggedIn.subscribe((loggedIn: boolean) => {
      this.isLoggedIn = loggedIn;
    });

    this.registerService.getAllSpecializations().subscribe((data) => {
      this.specializationsList = data;
      console.log('Loaded specializations:', this.specializationsList);
    });
    this.citySearchService.getCitySearchObservable(this.cityControl)
      .subscribe({
        next: ({ cities, noResults }) => {
          this.filteredCities = cities;
          this.isLoading = false;
          this.showDropdown = cities.length > 0 && this.cityControl.value.length >= 2;
        },
        error: () => {
          this.isLoading = false;
          this.showDropdown = false;
        }
      });
  }
  selectCity(city: string) {
    this.cityControl.setValue(city);
    this.showDropdown = false;
  }

  handleFocus() {
    if (this.cityControl.value?.length >= 2) {
      this.showDropdown = true;
    }
  }
  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
    
  }
  isLoginRoute(): boolean {
    return this.router.url === '/login';
  }
  isRegisterRoute(): boolean {
    return this.router.url === '/register';
  }

  isDefaultRoute(): boolean {
    return this.router.url === '/';
  }
  isContactRoute(): boolean {
    return this.router.url == '/contact';
  }
  isBlogRoute(): boolean {
    return this.router.url == '/blog' || this.router.url == '/results';
  }
  isExpertsRoute(): boolean {
    return this.router.url == '/experts';
  }
}
