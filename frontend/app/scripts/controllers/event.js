'use strict';

angular.module('frontendApp')
  .controller('EventCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.awesomeThings = [ 'HTML5 Boilerplate', 'AngularJS', 'Karma' ];
    $scope.eventId = $routeParams.eventId;
  }]);
