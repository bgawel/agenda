'use strict';

angular.module('frontendApp')
  .controller('SignupConfirmationCtrl', ['$scope', '$routeParams', function ($scope, $routeParams) {
      $scope.type = $routeParams.type;
  }]);
