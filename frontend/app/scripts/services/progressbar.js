'use strict';

angular.module('frontendApp').factory('Progressbar', ['$modal', function($modal) {
  return {
    open : function(scope) {
      var progress = scope.$progress = $modal.open({
        templateUrl: 'template/loadingbar/loadingbar.html',
        scope: scope,
        backdrop: false,
        windowClass: ['loadingbar']
      });
      return progress;
    },
    close : function(scope) {
      scope.$progress.close();
    }
  };
}]);