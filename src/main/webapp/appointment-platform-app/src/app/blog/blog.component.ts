import { Component } from '@angular/core';
import { Card } from '../models/card.model';
import { CardComponent } from '../shared/card/card.component';
import { ButtonPrimaryComponent } from "../shared/button-primary/button-primary.component";

@Component({
  selector: 'app-blog',
  imports: [CardComponent, ButtonPrimaryComponent],
  templateUrl: './blog.component.html',
  styleUrl: './blog.component.css',
})
export class BlogComponent {
  cards: Card[] = [
    {
      id: '01',
      icon: 'img/how_it_works_icon_1.png',
      title: 'Zadajesz pytanie',
      content:
        'Wpisujesz krótkie pytanie dotyczące Twoich wątpliwości finansowych.',
    },
    {
      id: '02',
      icon: 'img/how_it_works_icon_2.png',
      title: 'Pytanie jest przesyłane do ekspertów',
      content:
        'Po weryfikacji przez moderatora pytanie trafia do odpowiednich specjalistów.',
    },
    {
      id: '03',
      icon: 'img/how_it_works_icon_3.png',
      title: 'Ekspert odpowiada',
      content:
        'Zwykle na Twoje pytanie odpowiada kilku doradców i ekspertów finansowych.',
    },
    {
      id: '04',
      icon: 'img/how_it_works_icon_4.png',
      title: 'Czytasz odpowiedź',
      content: 'O otrzymanych odpowiedziach powiadomimy Cię e-mailem.',
    },
  ];
}
