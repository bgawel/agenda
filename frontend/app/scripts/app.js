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
        reloadOnSearch: false,
        resolve: {
          checkLoggedin: ['Auth', function(Auth) { return Auth.assert(); }]
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
      .when('/rc/invalid', {
        templateUrl: 'views/invalidConfirmation.html',
        controller: 'InvalidConfirmationCtrl'
      })
      .when('/rc/signup/:type', {
        templateUrl: 'views/signupConfirmation.html',
        controller: 'SignupConfirmationCtrl'
      })
      .when('/rc/setPwd/:token/:username', {
        templateUrl: 'views/setPwd.html',
        controller: 'SetPwdCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
    $httpProvider.interceptors.push('ResponseInterceptor');
  }])
  .run(['$location', '$rootScope', 'I18n', '$growl', '$modalStack', '$route',
        function ($location, $rootScope, I18n, $growl, $modalStack, $route) {
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
      if (rejection && rejection.status === 666) {
        gotoLoginPage(rejection);
      }
    });
    $rootScope.$on('onUnexpectedServerError', function(event, rejection) {
      var text
      switch (rejection.status) {
        case 401:
        case 403:
          gotoLoginPage(rejection);
          break;
        case 404:
          text = I18n.error.status404
          break;
        case 0: // status = 0 as well when response to xhr and status - 302 
          text = I18n.error.status0
          break;
        default:
          text = I18n.error.unexpected
      }
      text && $growl.box(null, text, {
                class: 'danger',
                sticky: false,
                timeout: 10000
              }).open();
    });
    
    var gotoLoginPage = function(rejection) {
      if ($location.path() !== '/') { // main page check passes through 
        $modalStack.dismissAll();
        var params = $route.current.params;
        $location.url('login');
        delete params.instId;
        $location.search(params);
      }
    };
    
  }]);
