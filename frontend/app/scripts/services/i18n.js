'use strict';

angular.module('frontendApp')
  .factory('I18n', function() {
    return {
      forLang : function(lang) {
        if (lang === 'pl') {
          return {
            error : {
              unexpected: 'Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - agenda.wro@gmail.com',
              status0: 'Wystąpił problem połączenia z serwerem',
              status404: 'Przykro nam, ale obiekt nie jest dostępny',
              email: 'Podaj poprawny adres e-mail',
              pwd: 'Podaj hasło',
              newPwd: 'Podaj hasło: min. 5 znaków',
              confirmNewPwd: 'Potwierdź hasło',
              confirmPwd: 'Podana wartość nie jest równa hasłu',
              newPwdSameAs: 'Podana wartość nie jest równa nowemu hasłu',
              event: 'Podaj tytuł wydarzenia',
              picFormat: 'Niedozwolnony format, wybierz jpg, png, jpeg, bmp albo gif',
              picSize: 'Rozmiar musi być mniejszy niż 3 MB',
              lnkOrDesc: 'Opisz wydarzenie lub podaj przynajmniej link do opisu w polu \'Link do opisu\'',
              category: 'Wybierz kategorię',
              where: 'Podaj miejsce, gdzie odbędzie się wydarzenie',
              price: 'Podaj cenę',
              date: 'Podaj termin wydarzenia',
              badDate: 'Podany termin jest niepoprawny',
              time: 'Podaj czas rozpoczęcia wydarzenia',
              timeDesc: 'Opisz czas realizacji wydarzenia',
              timeDescStart: 'Podaj poprawną godzinę',
              inst: 'Podaj nazwę organizatora'
            },
            placeholder : {
              email: 'adres e-mail',
              pwd: 'hasło',
              minPwd: 'min. 5 znaków',
              event: 'nazwa wydarzenia',
              lnkToDesc: 'np. www.twoja-strona.pl/to-wydarzenie',
              desc: 'maksymalnie 1800 znaków',
              oneTimeType: 'jednorazowe (np. przedstawienie, koncert, film)',
              tmpType: 'tymczasowe (np. wystawa, festiwal, targi)',
              checkType: 'Sprawdź obie opcje i wybierz dogodniejszą dla wydarzenia',
              where: 'lokalizacja wydarzenia, np. Mała Scena, Świętego Antoniego 651',
              price: 'np. 40zł (norm.), 30zł (ulg.)',
              date: 'np. 21-12-2017',
              toDate: 'np. 24-12-2017',
              timeDesc: 'np. wernisaż: 7 lutego, piątek, o godz. 18:00, pon.-pią.: 13.00-20.00, sob.: 11.00-15.00',
              timeDescStart1: 'Podaj najwcześniejszą godzinę rozpoczęcia wydarzenia',
              timeDescStart2: '(w powyższym przykładzie: 11:00)',
              inst: 'nazwa organizatora, np. Teatr Smacznego Obwarzanka',
              loginEmail: 'adres e-mail, który będzie używany do logowania',
              instDetails: 'Użytkownik będzie mógł wyświetlić poniższe dane przy wydarzeniu'
            },
            calendar : {
              format: 'dd-MM-yyyy',
              time: 'HH:mm',
              shortFormat: 'dd.MM',
              today: 'Dziś',
              weeks: 'Tygodnie'
            },
            timeFormat: 'HH:mm',
            action : {
              close: 'Zamknij',
              clear: 'Wyczyść',
              change: 'Zmień',
              cancel: 'Anuluj',
              save: 'Zapisz',
              edit: 'Edytuj',
              send: 'Wyślij',
              remove: 'Usuń',
              ok: 'Ok',
              yes: 'Tak',
              no: 'Nie'
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
              changePwd: 'Zmiana hasła',
              setNewPwd: 'Zmień hasło',
              currentPwd: 'Bieżące hasło',
              newPwd: 'Nowe hasło',
              confirmNewPwd: 'Potwierdź nowe hasło',
              resetPwd: 'Link umożliwiający ustanowienie nowego hasła został wysłany na Twój adres e-mail',
              pwdChanged: 'Hasło zostało zmienione'
            },
            resetPwd : {
              title: 'Resetowanie hasła',
              question: 'Czy wysłać na poniższy e-mail link umożliwiający ustanowienie nowego hasła?',
              email: 'adres e-mail używany do logowania'
            },
            setPwd : {
              title: 'Ustanowienie nowego hasła',
              confirm: 'Potwierdź nowe hasło'
            },
            appName: 'kulturalna AGENDA Wrocławia',
            doneBy: 'bgawel',
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
                about: 'O tym demo'
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
              others: 'Pozostałe terminy wydarzenia',
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
              },
              title: 'Tytuł',
              pic: 'Zdjęcie',
              lnkToDesc: 'Link do opisu',
              andOr: 'oraz/lub',
              desc: 'Opis',
              category: 'Kategoria',
              chooseCategory: 'wybierz kategorię',
              type: 'Typ wydarzenia',
              next: 'Następne wystąpienie',
              remove: 'Usuń to wystąpienie',
              deleteEvent: 'Usuń wydarzenie',
              when: 'Kiedy',
              time: 'O godzinie',
              fromDate: 'Od',
              toDate: 'do',
              timeDesc: 'O godzinie',
              timeDescStart: 'Najwcześniej o',
              mod: 'Zmodyfikowano'
            },
            inst : {
              del : {
                title: 'Usunięcie konta organizatora',
                query: 'Czy na pewno chcesz usunąć konto razem z wszystkimi zgłoszonymi wydarzeniami?',
                confirm: 'Konto zostało usunięte wraz z wszystkimi danymi.'
              },
              name: 'Nazwa',
              email: 'E-mail (login)',
              address: 'Adres',
              web: 'Strona domowa',
              phone: 'Telefon',
              deleteInst: 'Usuń konto'
            },
            about : {
              text: 'RESTful demo wyświetlające wydarzenia kulturalne. Organizatorzy wydarzeń zarządzają nimi w panelu administracyjnym.',
              src: 'Kod',
              contact: 'Kontakt',
              xray: 'Rentgen projektu',
              client: 'klient',
              server: 'serwer',
            },
            signup : {
              title: 'Rejestracja organizatora',
              tillNow: 'Dotychczas zarejestrowali się',
              pwd: 'Hasło',
              newPwd: 'Potwierdź hasło'
            },
            confirm : {
              invalid : {
                title: 'Nieprawidłowe potwierdzenie',
                msg: 'Przykro nam, ale Twoje potwierdzenie wygasło. Zgłoś nowe.'
              },
              signup : {
                title: 'Potwierdzenie rejestracji',
                msg: 'Dziękujemy za potwierdzenie rejestracji.',
                login1: 'Możesz zalogować się',
                login2: 'tutaj',
                forward: 'E-mail z prośbą o potwierdzenie został wysłany do użytkownika'
              }
            },
            panel : {
              title: 'Panel administracyjny',
              menu : {
                inst: 'Dane organizatora',
                newEvent: 'Nowe wydarzenie',
                issuedEvents: 'Zgłoszone wydarzenia'
              }
            }
          }
        } else {
          return {
            error : {
              unexpected: 'Oops. An unexpected error, try again or let us know about a problem - agenda.wro@gmail.com',
              status0: 'A connection problem occurred',
              status404: 'Sorry but an object is no longer available',
              email: 'Type a correct email address',
              pwd: 'Type a password',
              newPwd: 'Type a password: min. 5 characters',
              confirmNewPwd: 'Confirm a password',
              confirmPwd: 'A given value is not the same as a password',
              newPwdSameAs: 'A given value is not the same as a new password',
              event: 'Type a title of event',
              picFormat: 'Incorrect format, choose jpg, png, jpeg, bmp or gif',
              picSize: 'Size must be less than 3 MB',
              lnkOrDesc: 'Describe an event or type at least a link to description in a field \'Link to description\'',
              category: 'Choose a category',
              where: 'Type where an event happens',
              price: 'Type a price',
              date: 'Specify a date when an event starts',
              badDate: 'Incorrect date format',
              time: 'Specify what time an event starts',
              timeDesc: 'Describe time range of event',
              timeDescStart: 'Incorrect hour format',
              inst: 'Type a name of institution'
            },
            placeholder : {
              email: 'email address',
              pwd: 'password',
              minPwd: 'min. 5 characters',
              event: 'event\'s name',
              lnkToDesc: 'e.g. www.your-web.com/this-event',
              desc: 'max. 1800 characters',
              oneTimeType: 'single (e.g. show, concert, movie)',
              tmpType: 'temporal (e.g. exhibition, festival, fair)',
              checkType: 'Check both options and choose a better one for your event',
              where: 'event localization, e.g. 407 West 42nd Street',
              price: 'e.g. 40$ (full price), 30$ (reduced)',
              date: 'e.g. 21-12-2017',
              toDate: 'e.g. 24-12-2017',
              timeDesc: 'e.g. Mo - Fr: 13.00-20.00, Sa: 11.00-15.00',
              timeDescStart1: 'Earliest hour when an event starts',
              timeDescStart2: '(for the above example: 11:00)',
              inst: 'name of your institution',
              loginEmail: 'email address to be used to sign in',
              instDetails: 'A user will be able to display the following data at event'
            },
            calendar : {
              format: 'dd-MM-yyyy',
              time: 'HH:mm',
              shortFormat: 'dd.MM',
              today: 'Today',
              weeks: 'Weeks'
            },
            timeFormat: 'HH:mm',
            action : {
              close: 'Close',
              clear: 'Clear',
              change: 'Change',
              cancel: 'Cancel',
              save: 'Save',
              edit: 'Edit',
              send: 'Send',
              remove: 'Delete',
              ok: 'OK',
              yes: 'Yes',
              no: 'No'
            },
            status : {
              updated: 'The data has been updated'
            },
            login : {
              heading: 'Sign in',
              rememberMe: 'Remember me',
              remindMe: 'Reset password',
              signin: 'Sign in',
              signout: 'Sign out',
              signup: 'Sign up',
              hasAccount: 'Not yet registered?',
              pros: ['+ You will manage your events in an administration panel',
                     '+ A name of your institution will be shown on the main page',
                     '+ A user will be able to display data of your institution at event'],
              badCredentials: 'Email address or password is invalid',
              changePwd: 'Change password',
              setNewPwd: 'Change password',
              currentPwd: 'Current password',
              newPwd: 'New password',
              confirmNewPwd: 'Retype a new password',
              resetPwd: 'A link that allows to set a new password has been sent to your email address',
              pwdChanged: 'Your password has been changed'
            },
            resetPwd : {
              title: 'Reset password',
              question: 'Type an email address to receive a link that allows to reset your password',
              email: 'email address used as login'
            },
            setPwd : {
              title: 'Set new password',
              confirm: 'Retype a new password'
            },
            appName: 'Agenda',
            doneBy: 'bgawel',
            main : {
              nav : {
                toggle: 'Toggle',
                filters: 'Filters',
                announcements: 'Announcements'
              },
              menu : {
                events: 'Events',
                addEvent: 'Add a new event',
                adminPanel: 'Administration panel',
                about: 'About this demo'
              },
              categories: 'Categories',
              insts: 'Institutions for the category and date',
              sort : {
                title: 'Sort by',
                byTime: 'time',
                byTitle: 'title'
              },
              noEvents: 'No events for this date',
              soon: 'Coming soon',
              newest: 'Last added'
            },
            event : {
              where: 'Where',
              price: 'Price',
              more: 'More',
              others: 'Other occurrences',
              del : {
                title: 'Delete event',
                query: 'Do you really want to delete this event?',
                confirm: 'The event has been deleted.'
              },
              uploadError: 'Cannot upload an image',
              addedNew: 'A new event has been added',
              changeType : {
                title: 'Change a type of event',
                query: 'Do you really want to change a type of event? Some data regarding dates will be lost during conversion.'
              },
              title: 'Title',
              pic: 'Image',
              lnkToDesc: 'Link to description',
              andOr: 'and/or',
              desc: 'Description',
              category: 'Category',
              chooseCategory: 'choose category',
              type: 'Type of event',
              next: 'Next occurrence',
              remove: 'Delete this occurrence',
              deleteEvent: 'Delete event',
              when: 'When',
              time: 'At',
              fromDate: 'From',
              toDate: 'to',
              timeDesc: 'At',
              timeDescStart: 'Earliest at',
              mod: 'Last modified'
            },
            inst : {
              del : {
                title: 'Delete account',
                query: 'Do you really want to delete your account with all issued events?',
                confirm: 'Your account has been deleted with all data.'
              },
              name: 'Name',
              email: 'E-mail (login)',
              address: 'Address',
              web: 'Web',
              phone: 'Phone',
              deleteInst: 'Delete account'
            },
            about : {
              text: 'This RESTful application displays cultural events. ' + 
                'Institutions that organize events manage their events in an administration panel.',
              src: 'Source code',
              contact: 'Contact',
              xray: 'X-Ray of this project',
              client: 'client',
              server: 'server',
            },
            signup : {
              title: 'Registration',
              tillNow: 'Already registered',
              pwd: 'Password',
              newPwd: 'Retype password'
            },
            confirm : {
              invalid : {
                title: 'Invalid confirmation',
                msg: 'Sorry but your confirmation request expired. Submit a new one.'
              },
              signup : {
                title: 'Confirmation of registration',
                msg: 'Thank your for registration.',
                login1: 'You can sign in',
                login2: 'here',
                forward: 'A confirmation request has been sent to a user'
              }
            },
            panel : {
              title: 'Administration panel',
              menu : {
                inst: 'Institution',
                newEvent: 'New event',
                issuedEvents: 'Issued events'
              }
            }
          } 
        }
      }
    };
  });