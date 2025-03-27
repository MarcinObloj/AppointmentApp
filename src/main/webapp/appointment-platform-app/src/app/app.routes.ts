import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { MainPageComponent } from './main-page/main-page.component';
import { ContactComponent } from './main-page/contact/contact.component';
import { ContactPageComponent } from './contact-page/contact-page.component';
import { FindExpertsComponent } from './find-experts/find-experts.component';
import { BlogComponent } from './blog/blog.component';
import { ResultsExpertsComponent } from './find-experts/results-experts/results-experts.component';
import { ProfileComponent } from './profile/profile.component';

export const routes: Routes = [
  { path: '', component: MainPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'contact', component: ContactPageComponent },
  { path: 'experts', component: FindExpertsComponent },
  { path: 'blog', component: BlogComponent },
  { path: 'results', component: ResultsExpertsComponent },
  { path: 'profile/:id', component: ProfileComponent},
];
