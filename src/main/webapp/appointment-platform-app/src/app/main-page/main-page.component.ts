import { Component } from '@angular/core';
import {FooterComponent} from '../shared/footer/footer.component';
import {ContactComponent} from './contact/contact.component';
import {OpinionsComponent} from './opinions/opinions.component';
import {OurServicesComponent} from './our-services/our-services.component';
import {BestExpertsComponent} from './best-experts/best-experts.component';
import {HowItWorksComponent} from './how-it-works/how-it-works.component';
import {HeaderComponent} from './header/header.component';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  imports: [
    FooterComponent,
    ContactComponent,
    OpinionsComponent,
    OurServicesComponent,
    BestExpertsComponent,
    HowItWorksComponent,
    HeaderComponent
  ],
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent {}
