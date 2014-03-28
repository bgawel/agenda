'use strict';

angular.module('frontendApp').factory('Progressbar', ['$modal', '$rootScope', function($modal, $rootScope) {
  return {
    open : function() {
      if ($rootScope.$progress) {
        return;
      }
      var progress = $rootScope.$progress = $modal.open({
        templateUrl: 'template/loadingbar/loadingbar.html',
        scope: $rootScope,
        backdrop: false,
        windowClass: ['loadingbar']
      });
      return progress;
    },
    close : function() {
      $rootScope.$progress && $rootScope.$progress.close();
      $rootScope.$progress = null;
    }
  };
}]);