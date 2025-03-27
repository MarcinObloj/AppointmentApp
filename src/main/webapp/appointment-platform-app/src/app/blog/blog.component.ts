import { Component, inject, OnInit } from '@angular/core';
import { Card } from '../models/card.model';
import { CardComponent } from '../shared/card/card.component';
import { ButtonPrimaryComponent } from '../shared/button-primary/button-primary.component';
import { QuestionService } from './question.service';
import { Answer, Question } from '../models/question.model';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Expert } from '../models/expert.model';
import { LoginResponse } from '../models/loginResponse.model';
import { AnswerService } from './answer.service';

@Component({
  selector: 'app-blog',
  imports: [CardComponent, ButtonPrimaryComponent, FormsModule],
  templateUrl: './blog.component.html',
  styleUrl: './blog.component.css',
})
export class BlogComponent implements OnInit {
  questionService = inject(QuestionService);
  authService = inject(AuthService);
  answerService = inject(AnswerService);
  questions: Question[] = [];
  isExpert: boolean = false;
  currentExpert: LoginResponse | null = null;
  newAnswer: { [key: string]: string } = {};
  ngOnInit(): void {
    this.questionService.getQuestions().subscribe((data) => {
      this.questions = data;
      console.log(data);
    });
    this.authService.isLoggedIn.subscribe((isLoggedIn) => {
      if (isLoggedIn) {
        // Sprawdzamy rolę użytkownika (jeśli masz tę informację w sessionStorage
        const role = sessionStorage.getItem('role');
        if (role === 'EXPERT') {
          this.isExpert = true;
          const expertJson = sessionStorage.getItem('expert');
          if (expertJson) {
            this.currentExpert = JSON.parse(expertJson);
          }
        }
      }
    });
  }
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
  submitAnswer(questionId: number): void {
    if (this.currentExpert) {
      const answer: Answer = {
        createdAt: new Date().toISOString(),
        content: this.newAnswer[questionId],
        questionId: questionId,
        expertId: this.currentExpert.expert!.id,
      };
      this.answerService
        .addAnswer(answer, questionId, this.currentExpert.expert!.id)
        .subscribe((response) => {
          console.log('Answer added:', response);
          // Odśwież pytania po dodaniu odpowiedzi
          this.questionService.getQuestions().subscribe((data) => {
            this.questions = data; 
            console.log('Updated questions:', this.questions);
          });
        });
    }
  }
}
