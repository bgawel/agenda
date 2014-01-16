'use strict';

angular.module('frontendApp')
  .controller('EventCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.eventId = $routeParams.eventId;
  }]);
