import { Component } from '@angular/core';
import {ButtonPrimaryComponent} from '../../shared/button-primary/button-primary.component';

@Component({
  selector: 'app-header',
  imports: [
    ButtonPrimaryComponent
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

}
