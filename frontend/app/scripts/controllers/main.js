'use strict';

angular.module('frontendApp')
  .controller('MainCtrl',  ['$scope', 'Metadata', function($scope, Metadata) {
    $scope.toggleRightPanel = function () {
      $scope.rightPanel = ($scope.rightPanel === 'activeRight') ? '' : 'activeRight';
    };
    $scope.toggleLeftPanel = function () {
      $scope.leftPanel = ($scope.leftPanel === 'activeLeft') ? '' : 'activeLeft';
    };
    
    var activateEventsDay = function(dayIndex) {
      $scope.weekMenu.activeIndex = dayIndex;
      $scope.weekMenu[dayIndex].ngClass = 'active';
    };
    $scope.weekMenu = Metadata.week(function() {
      var currentDayIndex = 1;
      activateEventsDay(currentDayIndex);
    });
    $scope.changeEventsDay = function(dayIndex) {
      $scope.weekMenu[$scope.weekMenu.activeIndex].ngClass = '';
      activateEventsDay(dayIndex);
    };
    
    $scope.event = {id:6};
  }]);
