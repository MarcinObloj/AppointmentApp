import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonPrimaryComponent } from '../button-primary/button-primary.component';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, ButtonPrimaryComponent, RouterLink],
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
})
export class NavComponent  implements OnInit {
  isOpen = false;
  router = inject(Router);
  authService = inject(AuthService);
  isLoggedIn = false;
  specializations: string[] = ['Prawnik', 'Lekarz', 'Inżynier', 'Nauczyciel'];
  cities: string[] = ['Warszawa', 'Kraków', 'Wrocław', 'Poznań'];
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
