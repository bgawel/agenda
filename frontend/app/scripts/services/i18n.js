'use strict';

angular.module('frontendApp')
  .factory('I18n', function() {
    return {
      error : {
        unexpected: 'Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl',
        status0: 'Wystąpił problem połączenia z serwerem',
        status404: 'Przykro nam, ale obiekt nie jest dostępny',
        email: 'Podaj poprawny adres e-mail',
        pwd: 'Podaj hasło'
      },
      placeholder : {
        email: 'Adres e-mail',
        pwd: 'Hasło'
      },
      calendar : {
        format: 'dd-MM-yyyy',
        shortFormat: 'dd.MM',
        today: 'Dziś',
        weeks: 'Tygodnie'
      },
      timeFormat: 'HH:mm',
      action : {
        close: 'Zamknij',
        clear: 'Wyczyść'
      },
      status : {
        updated: 'Dane zostały zaktualizowane'
      },
      login : {
        heading: 'Zaloguj się',
        rememberMe: 'Zapamiętaj mnie',
        remindMe: 'Zresetuj hasło',
        signin: 'Zaloguj',
        signout: 'Wyloguj',
        signup: 'Zarejestruj się',
        hasAccount: 'Nie masz konta?',
        pros: ['+ Będziesz zarządzać wydarzeniami w panelu administracyjnym', 
               '+ Twoja organizacja będzie widoczna na głównej stronie na liście organizatorów',
               '+ Użytkownik będzie mógł wyświetlić dane Twojej organizacji przy wydarzeniu'],
        badCredentials: 'Adres email lub hasło są niepoprawne',
        resetPwd: 'Link umożliwiający ustanowienie nowego hasła został wysłany na Twój adres e-mail',
        statusPwdChanged: 'Hasło zostało zmienione'
      },
      appName: 'kulturalna AGENDA Wrocławia',
      doneBy: 'DaDa Soft Lab 2014',       
      main : {
        nav : {
          toggle: 'Nawigacja',
          filters: 'Filtry',
          announcements: 'Zapowiedzi' 
        },
        menu : {
          events: 'Wydarzenia',
          addEvent: 'Zgłoś wydarzenie',
          adminPanel: 'Panel administracyjny',
          about: 'O nas'
        },
        categories: 'Kategorie',
        insts: 'Organizatorzy w kategorii w wybranym terminie',
        sort : {
          title: 'Sortuj wg',
          byTime: 'czasu',
          byTitle: 'tytułu'
        },
        noEvents: 'Brak wydarzeń',
        soon: 'Wkrótce',
        newest: 'Najnowsze wpisy'
      },
      event : {
        where: 'Gdzie',
        price: 'Cena',
        more: 'Więcej',
        del : {
          title: 'Usunięcie wydarzenia',
          query: 'Czy na pewno chcesz usunąć wydarzenie?',
          confirm: 'Wydarzenie zostało usunięte.'
        },
        uploadError: 'Niestety z przyczyn technicznych nie można dodać zdjęcia',
        addedNew: 'Nowe wydarzenie zostało dodane',
        changeType : {
          title: 'Zmiana typu wydarzenia',
          query: 'Czy na pewno chcesz zmienić typ wydarzenia? Część danych dotycząca terminów zostanie utracona.'
        } 
      },
      inst : {
        del : {
          title: 'Usunięcie konta organizatora',
          query: 'Czy na pewno chcesz usunąć konto razem z wszystkimi zgłoszonymi wydarzeniami?',
          confirm: 'Konto zostało usunięte wraz z wszystkimi danymi.'
        }
      }
    };
  });