'use strict';

angular.module('frontendApp')
  .controller('EventCtrl', ['$scope', '$routeParams', '$location', 'Events',
                            function($scope, $routeParams, $location, Events) {
    Events.byId($routeParams.eventId).then(function(event) {
      $scope.event = event;
    }, function() {
      $location.url('/');
    });
  }]);
