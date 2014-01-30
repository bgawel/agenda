'use strict';

angular.module('frontendApp')
  .controller('EventCtrl', ['$scope', '$routeParams', 'Events', function($scope, $routeParams, Events) {
    Events.byId($routeParams.eventId).then(function(event) {
      $scope.event = event;
    });
  }]);
