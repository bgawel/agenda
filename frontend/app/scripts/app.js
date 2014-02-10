'use strict';

angular.module('frontendApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ui.bootstrap',
  'angularFileUpload'
])
  .config(['$routeProvider', '$provide', function ($routeProvider, $provide) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/event/:eventId', {
        templateUrl: 'views/event.html',
        controller: 'EventCtrl'
      })
      .when('/panel/:instId', {
        templateUrl: 'views/panel.html',
        controller: 'PanelCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .when('/signup', {
        templateUrl: 'views/signup.html',
        controller: 'SignupCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
    $provide.decorator('$httpBackend', angular.mock.e2e.$httpBackendDecorator);
  }])
 .run(function($httpBackend) {
    var regexpUrl = function(regexp) {
      return {
        test: function(url) {
          this.matches = url.match(regexp);
          return this.matches && this.matches.length > 0;
        }
      };
    };
    $httpBackend.when('GET', regexpUrl(/.*/)).passThrough();
    //$httpBackend.when('POST', regexpUrl(/.*/)).passThrough();
    $httpBackend.whenPOST('inst.json').respond(200,
        {success:true,
         id:1,
         messages:['Zapisano dane']
        });
    $httpBackend.whenPOST('event.json').respond(200,
        {success:true,
         id:1,
         messages:['Zapisano dane']
        });
  });
