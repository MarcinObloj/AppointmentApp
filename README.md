# FinExpert

FinExpert to aplikacja webowa, która umożliwia użytkownikom umawianie spotkan z ekspertami finansowymi. Klienci mogą również zadawać pytanie na blogu i czekać na odpowiedz od eksperta .Projekt składa się z frontendu (Angular, TypeScript) oraz backendu (Spring Boot, PostgreSQL).

## Funkcje

### Rejestracja i logowanie użytkowników
Użytkownicy (klienci i eksperci) mogą tworzyć konta i logować się do aplikacji, aby uzyskać dostęp do odpowiednich funkcji. 

### Umawianie spotkań
Klienci mogą wyszukiwać ekspertów na podstawie specjalizacji i lokalizacji, a następnie umawiać się na spotkania w dostępnych godzinach.

### Wyszukiwanie ekspertów
Klienci mogą wyszukiwać ekspertów według specjalizacji (np. finanse) oraz lokalizacji, aby łatwo znaleźć odpowiednią osobę do pomocy.

### Pytania i odpowiedzi
Klienci mogą zadawać pytania ekspertom, którzy mogą udzielać odpowiedzi w formie tekstowej na blogu.

### Weryfikacja email
Po rejestracji użytkownicy otrzymują email weryfikacyjny, aby potwierdzić swoje konto i rozpocząć korzystanie z aplikacji. Po umówionym spotkaniu również ekspert jak i klient otrzymuje odpowiednie powiadomienie na mailu.

### Autoryzacja JWT
Użycie tokenów JWT (JSON Web Tokens) zapewnia bezpieczną autoryzację użytkowników i ochronę danych.
Technologie
Frontend
<div style="display: flex; gap: 10px; align-items: center;"> 
  <img src="https://img.icons8.com/color/48/000000/angularjs.png" alt="Angular" title="Angular"/> 
  <img src="https://img.icons8.com/color/48/000000/typescript.png" alt="TypeScript" title="TypeScript"/> </div>
Backend
<div style="display: flex; gap: 10px; align-items: center;"> 
  <img src="https://img.icons8.com/color/48/000000/java-coffee-cup-logo.png" alt="Java" title="Java"/> 
  <img src="https://img.icons8.com/color/48/000000/spring-logo.png" alt="Spring Boot" title="Spring Boot"/> 
  <img src="https://img.icons8.com/color/48/000000/postgreesql.png" alt="PostgreSQL" title="PostgreSQL"/> 
  <img src="https://img.icons8.com/color/48/000000/swagger.png" alt="Swagger" title="Swagger"/> </div

## Dokumentacja SWAGGER
![Swagger](https://i.imgur.com/K3nwo4a.png)

## Przegląd aplikacji

### Strona główna:
![Strona_główna](https://i.imgur.com/GmjEQYn.png)
### Rejestracja:
![Rejestracja](https://i.imgur.com/J6Ao62H.png)
### Rejestracja eksperta:
![Rejestracja](https://i.imgur.com/IoTJ1NN.png)
### Login:
![Login](https://i.imgur.com/x3bKFcK.png)
### Blog:
![Blog](https://i.imgur.com/hFPX9mS.png)
### Profil eksperta:
![Profil_eksperta](https://i.imgur.com/HE42CFY.png)
### Wyszukiwanie eksperta:
![Wyszukiwanie_eksperta](https://i.imgur.com/tIgfvPD.png)
### Wyniki wyszukiwań ekspertów:
![Wyniki](https://i.imgur.com/vmimPY6.png)
### Wyniki wyszukiwań ekspertów:
![Kontakt](https://i.imgur.com/BmYk9IA.png)

## Jak uruchomić projekt lokalnie

### Wymagania wstępne

- Java 17 (lub nowsza)
- Node.js (opcjonalnie, do zarządzania zależnościami frontendu)
- PostgreSQL (lub inna baza danych obsługiwana przez Spring Boot)
- Maven (do zarządzania zależnościami backendu)

### Backend (Spring Boot)

1. Sklonuj repozytorium:

```bash
git clone https://github.com/MarcinObloj/FinExpert.git
```

2. Przejdź do folderu backendu:

```bash
cd FinExpert/backend
```

3. Skonfiguruj bazę danych:

Utwórz bazę danych PostgreSQL o nazwie `finexpert`.

Zaktualizuj plik `application.properties` w folderze `src/main/resources` z danymi do połączenia z bazą danych:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finexpert
spring.datasource.username=twoj-uzytkownik
spring.datasource.password=twoje-haslo
```

4. Uruchom aplikację Spring Boot:

```bash
mvn spring-boot:run
```

### Frontend (Angular)

1. Przejdź do folderu frontendu:

```bash
cd FinExpert/frontend
```

2. Zainstaluj zależności:

```bash
npm install
```

3. Uruchom aplikację Angular:

```bash
ng serve
```

4. Otwórz aplikację w przeglądarce:

Przejdź do `http://localhost:4200`, aby zobaczyć aplikację.
