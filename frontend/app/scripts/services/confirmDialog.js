'use strict';

angular.module('frontendApp').factory('ConfirmDialog', ['$modal', function($modal) {
  var GenericCtrl = function ($scope, $modalInstance, headerText, bodyText) {
    $scope.headerText = headerText;
    $scope.bodyText = bodyText;
    
    $scope.ok = function () {
      $modalInstance.close();
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  };
  var genericResolver = function(headerText, bodyText) {
    return {
      headerText: function() {
        return headerText;
      },
      bodyText: function() {
        return bodyText;
      }
    };
  };
  return {
    confirmDelete : function(scope, headerText, bodyText) {
      return $modal.open({
        templateUrl: 'template/confirmationdialog/delete.html',
        controller: GenericCtrl,
        resolve: genericResolver(headerText, bodyText),
        scope: scope,
        backdrop: false
      });
    },
    confirmInfo : function(scope, headerText, bodyText) {
      return $modal.open({
        templateUrl: 'template/confirmationdialog/info.html',
        controller: GenericCtrl,
        resolve: genericResolver(headerText, bodyText),
        scope: scope,
        backdrop: false,
        keyboard: false
      });
    },
    confirmQuestion : function(scope, headerText, bodyText) {
      return $modal.open({
        templateUrl: 'template/confirmationdialog/question.html',
        controller: GenericCtrl,
        resolve: genericResolver(headerText, bodyText),
        scope: scope,
        backdrop: false
      });
    },
  };
}]);