'use strict';

angular.module('frontendApp')
  .controller('EventCtrl', ['$scope', '$routeParams', 'Events', function($scope, $routeParams, Events) {
    Events.byId($routeParams.eventId, function() {
      $scope.eventGone = true;
    }).
    then(function(event) {
      $scope.eventGone = false;
      $scope.event = event;
    });
  }]);
