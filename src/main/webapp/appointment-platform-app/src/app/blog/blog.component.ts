import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Card } from '../models/card.model';
import { CardComponent } from '../shared/card/card.component';
import { ButtonPrimaryComponent } from '../shared/button-primary/button-primary.component';
import { QuestionService } from './question.service';
import { Question } from '../models/question.model';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';

import { LoginResponse } from '../models/loginResponse.model';
import { AnswerService } from './answer.service';
import { Answer, AnswerResponseDTO } from '../models/answer.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-blog',
  imports: [CardComponent, ButtonPrimaryComponent, FormsModule,RouterLink],
  templateUrl: './blog.component.html',
  styleUrl: './blog.component.css',
})
export class BlogComponent implements OnInit {
  questionService = inject(QuestionService);
  authService = inject(AuthService);
  answerService = inject(AnswerService);
  questions: Question[] = [];
  cdr = inject(ChangeDetectorRef);
  isExpert: boolean = false;
  currentExpert: LoginResponse | null = null;
  newAnswer: { [key: string]: string } = {};
  newQuestionContent: string = '';
  ngOnInit(): void {
    this.questionService.getQuestions().subscribe((data) => {
      this.questions = data;
      console.log(data);
    });
    this.authService.isLoggedIn.subscribe((isLoggedIn) => {
      if (isLoggedIn) {
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
  submitQuestion(): void {
    const clientId = this.authService.getClientId();
    console.log('Client ID z tokena:', clientId); 

    if (clientId && this.newQuestionContent.trim()) {
      this.questionService
        .addQuestion({
          clientId: clientId,
          content: this.newQuestionContent.trim(),
        })
        .subscribe({
          next: (question) => {
            this.questions = [...this.questions, question];
            this.newQuestionContent = '';
          },
          error: (error: any) => {
            console.error('Błąd dodawania pytania:', error);
          },
        });
    }
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
        .subscribe((response: AnswerResponseDTO) => {
          console.log('Answer added:', response);

          const questionIndex = this.questions.findIndex(
            (q) => q.id === questionId
          );
          if (questionIndex !== -1) {
            // Inicjalizuj tablicę answers jeśli nie istnieje
            if (!this.questions[questionIndex].answers) {
              this.questions[questionIndex].answers = [];
            }

           
            const newAnswer = {
              ...response,
              expert: {
                ...response.expert,
               
                user: response.expert.user || {
                  photoUrl: response.expert.photoUrl,
                  firstName: response.expert.firstName,
                  lastName: response.expert.lastName,
                },
              },
            };

            this.questions[questionIndex].answers!.push(newAnswer);

            // Utwórz nową referencję tablicy pytań
            this.questions = [...this.questions];
          }

          this.newAnswer[questionId] = '';
          this.cdr.detectChanges();
        });
    }
  }
}
