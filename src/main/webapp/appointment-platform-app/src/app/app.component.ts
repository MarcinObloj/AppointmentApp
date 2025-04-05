import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NavComponent} from './shared/nav/nav.component';
import {HeaderComponent} from './main-page/header/header.component';
import {HowItWorksComponent} from './main-page/how-it-works/how-it-works.component';
import {OurServicesComponent} from './main-page/our-services/our-services.component';
import {FooterComponent} from './shared/footer/footer.component';
import {ContactComponent} from './main-page/contact/contact.component';
import {OpinionsComponent} from './main-page/opinions/opinions.component';
import {BestExpertsComponent} from './main-page/best-experts/best-experts.component';
import { AuthService } from './auth/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavComponent, HeaderComponent, HowItWorksComponent, OurServicesComponent, FooterComponent, ContactComponent, OpinionsComponent, BestExpertsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'appointment-platform-app';
  authService = inject(AuthService);
  ngOnInit(){
    this.authService.checkAuthState().subscribe();
  }
}
