'use strict';

angular.module('frontendApp')
  .factory('ResponseInterceptor', ['$q', '$rootScope', function($q, $rootScope) {
    var closeProgress = function() {
      $rootScope.$progress && $rootScope.$progress.close();
      $rootScope.$progress = null;
    };
    return {
      responseError: function(rejection) {
        closeProgress();
        if (rejection.status !== 401 && rejection.status !== 403 && rejection.status !== 422) {
          $rootScope.$broadcast('onUnexpectedServerError', rejection.status);
        }
        return $q.reject(rejection);
      }
    };
  }]);