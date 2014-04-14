'use strict';

angular.module('frontendApp')
  .controller('EventCtrl', ['$scope', '$routeParams', '$location', 'Events', 'Progressbar',
                            function($scope, $routeParams, $location, Events, Progressbar) {
    Progressbar.open();
    Events.byId($routeParams.eventId).then(function(event) {
      $scope.event = event;
      Progressbar.close();
    }, function() {
      $location.url('/');
    });
  }]);
