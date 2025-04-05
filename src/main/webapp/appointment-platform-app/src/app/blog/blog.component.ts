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
import { ModalComponent } from '../shared/modal/modal.component';
import { Page } from './blog.service';

interface CurrentUser{
  userId: number;
  role: string;
  firstName: string;
  lastName: string;
  photoUrl: string;
  expert?:{
    id: number | undefined;
    street: string | undefined;
    city: string |undefined;
  }
}

@Component({
  selector: 'app-blog',
  imports: [
    CardComponent,
    ButtonPrimaryComponent,
    FormsModule,
    RouterLink,
    ModalComponent,
  ],
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
  showModal = false;
  modalMessage = '';
  isModalError = false;
  currentPage = 0;
  pageSize = 5; 
  totalQuestions = 0;
  isLoading = true; // Dodaj flagę ładowania
  currentExpert: CurrentUser | null = null;
  newAnswer: { [key: string]: string } = {};
  newQuestionContent: string = '';
  ngOnInit(): void {
    this.loadQuestions();
    this.authService.getCurrentUser().subscribe(user => {
      this.isExpert = user.role === 'ROLE_EXPERT';
      console.log(user);
  
      // Mapowanie user -> LoginResponse
      this.currentExpert = {
        role: user.role,
        userId: user.id,  // Mapa `id` na `userId`
        firstName: user.firstName,
        lastName: user.lastName,
        photoUrl: user.photoUrl,
        expert: user.role === 'ROLE_EXPERT' ? {
          id: user.expert?.id,
          street: user.expert?.street,
          city: user.expert?.city
        } : undefined
      };
  
      this.cdr.detectChanges(); // Odświeżenie widoku
    });
  }
  
  loadQuestions(page: number = 0): void {
    this.questionService.getPaginatedQuestions(page, this.pageSize).subscribe({
      next: (page: Page<Question>) => {
        this.questions = page.content;
        this.totalQuestions = page.totalElements;
        this.currentPage = page.number;
      },
      error: (error) => {
        console.error('Error loading questions:', error);
        this.modalMessage = 'Wystąpił błąd podczas ładowania pytań';
        this.isModalError = true;
        this.showModal = true;
        setTimeout(() => this.showModal = false, 3000);
      }
    });
  }
  nextPage(): void {
    if ((this.currentPage + 1) * this.pageSize < this.totalQuestions) {
      this.loadQuestions(this.currentPage + 1);
    }
  }
  
  prevPage(): void {
    if (this.currentPage > 0) {
      this.loadQuestions(this.currentPage - 1);
    }
  }
  getPageNumbers(): number[] {
    const totalPages = Math.ceil(this.totalQuestions / this.pageSize);
    const pages = [];
    
    
    const maxVisiblePages = 5;
    let startPage = Math.max(0, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxVisiblePages - 1);
    
    
    startPage = Math.max(0, endPage - maxVisiblePages + 1);
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }
  goToPage(page: number): void {
    this.loadQuestions(page);
  }
  submitQuestion(): void {
    this.authService.getClientId().subscribe((clientId) => {
      if (clientId && this.newQuestionContent.trim()) {
        this.questionService
          .addQuestion({
            clientId: clientId,
            content: this.newQuestionContent.trim(),
          })
          .subscribe({
            next: () => {
              this.newQuestionContent = '';
              this.modalMessage = 'Pytanie zostało pomyślnie dodane!';
              this.isModalError = false;
              this.showModal = true;
              setTimeout(() => (this.showModal = false), 3000);
              
             
              this.loadQuestions(this.currentPage);
            },
          
          });
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
    if (this.currentExpert && this.currentExpert.expert && this.currentExpert.expert.id !== undefined) {
      const answer: Answer = {
        createdAt: new Date().toISOString(),
        content: this.newAnswer[questionId],
        questionId: questionId,
        expertId: this.currentExpert.expert.id, // Bezpieczne pobranie ID eksperta
      };
  
      this.answerService
        .addAnswer(answer, questionId, this.currentExpert.expert.id)
        .subscribe((response: AnswerResponseDTO) => {
          console.log('Answer added:', response);
  
          const questionIndex = this.questions.findIndex((q) => q.id === questionId);
          if (questionIndex !== -1) {
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
            this.questions = [...this.questions];
          }
  
          this.newAnswer[questionId] = '';
          this.cdr.detectChanges();
        });
    } else {
      console.error("User is not an expert or expert data is missing");
    }
  }
  
}
