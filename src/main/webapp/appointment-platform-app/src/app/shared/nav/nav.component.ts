import { Component, inject, OnInit } from '@angular/core';

import { ButtonPrimaryComponent } from '../button-primary/button-primary.component';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { Specialization } from '../modal/specialization.model';
import { RegisterService } from '../../auth/register/register.service';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [ButtonPrimaryComponent, RouterLink,NgClass],
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
})
export class NavComponent  implements OnInit {
  isOpen = false;
  router = inject(Router);
  authService = inject(AuthService);
  isLoggedIn = false;
  cities: string[] = ['Warszawa', 'Kraków', 'Wrocław', 'Poznań'];
  specializationsList:Specialization[]= [];
  registerService= inject(RegisterService);
   
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
