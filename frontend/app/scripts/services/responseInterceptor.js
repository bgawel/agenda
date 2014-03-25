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
        if (rejection.status !== 422 && rejection.status !== 410) {
          $rootScope.$broadcast('onUnexpectedServerError', rejection);
        }
        return $q.reject(rejection);
      }
    };
  }]);