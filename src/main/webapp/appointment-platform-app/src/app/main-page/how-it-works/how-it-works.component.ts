import { Component } from '@angular/core';
import {CardComponent} from '../../shared/card/card.component';
import {Card} from '../../models/card.model';

@Component({
  selector: 'app-how-it-works',
  imports: [
    CardComponent
  ],
  templateUrl: './how-it-works.component.html',
  styleUrl: './how-it-works.component.css'
})
export class HowItWorksComponent {
  cards: Card[] = [
    {
      id: '01',
      icon: 'img/loop-circle.png',
      title: 'Wyszukaj eksperta z naszej bazy',
      content: 'Przeglądaj listę dostępnych ekspertów według lokalizacji, specjalizacji, ocen i języka.',
    },
    {
      id: '02',
      icon: 'img/clock.png',
      title: 'Sprawdź opinie i dostępność',
      content: 'Zobacz recenzje klientów oraz dostępne terminy spotkań.',
    },
    {
      id: '03',
      icon: 'img/clock.png',
      title: 'Wybierz eksperta i umów się na wizytę',
      content: 'Wybierz dogodny termin i potwierdź rezerwację w kilku prostych krokach.',
    },
    {
      id: '+',
      icon: 'img/question.png',
      title: 'Zadaj pytanie na blogu i uzyskaj odpowiedź',
      content: 'Zadaj pytanie na blogu i uzyskaj odpowiedź od eksperta.',
    },
  ];
}
