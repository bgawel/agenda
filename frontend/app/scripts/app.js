'use strict';

angular.module('frontendApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ui.bootstrap',
  'ui-templates',
  'angularFileUpload',
  'ui.growl',
  'truncate'
])
  .config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
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
        controller: 'PanelCtrl',
        resolve: {
          checkLoggedin: ['Auth', function(Auth) { return Auth.check(); }]
        }
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
    $httpProvider.interceptors.push('ResponseInterceptor');
  }])
  .run(['$location', '$rootScope', 'I18n', '$growl', function ($location, $rootScope, I18n, $growl) {
    $rootScope.i18n = I18n;
    $rootScope.userId = -666;
    $rootScope.$on('onAuthenticationSuccess', function(event, data) {
      $rootScope.userId = data.id;
      $rootScope.username = data.username;
    });
    $rootScope.$on('onLogoutSuccess', function() {
      $rootScope.userId = -666;
    });
    $rootScope.$on('$routeChangeError', function(event, current, previous, rejection) {
      if (rejection && (rejection.status === 401 || rejection.status === 403)) {
        var panelOption = current.params.o;
        $location.url('login' + (panelOption ? ('?o=' + panelOption) : ''));
      }
    });
    $rootScope.$on('onUnexpectedServerError', function(event, status) {
      var text = status === 404 ? I18n.error.status404 : (status === 0 ? I18n.error.status0 : I18n.error.unexpected)
      $growl.box(null, text, {
        class: 'danger',
        sticky: false,
        timeout: 5000
      }).open();
    });
  }]);
