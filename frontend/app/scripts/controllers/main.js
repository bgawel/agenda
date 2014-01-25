'use strict';

angular.module('frontendApp')
  .controller('MainCtrl',  ['$scope', 'Metadata', 'Events', function($scope, Metadata, Events) {
    $scope.toggleRightPanel = function () {
      $scope.rightPanel = ($scope.rightPanel === 'activeRight') ? '' : 'activeRight';
    };
    $scope.toggleLeftPanel = function () {
      $scope.leftPanel = ($scope.leftPanel === 'activeLeft') ? '' : 'activeLeft';
    };
    
    var activateEventsDay = function(dayIndex) {
      $scope.weekMenu.activeIndex = dayIndex;
      var activeDay = $scope.weekMenu[dayIndex];
      activeDay.ngClass = 'active';
      Events.byDate(activeDay.id);
    };
    Metadata.week(function(data) {
      $scope.weekMenu = data.entries;
      activateEventsDay(data.activeIndex);
    });
    $scope.changeEventsDay = function(dayIndex) {
      $scope.weekMenu[$scope.weekMenu.activeIndex].ngClass = '';
      activateEventsDay(dayIndex);
    };
    
    var activateCategory = function(categoryIndex) {
      $scope.categoryFilter.activeIndex = categoryIndex;
      $scope.categoryFilter[categoryIndex].ngClass = 'active';
    };
    Metadata.categories(function(data) {
      $scope.categoryFilter = data.entries;
      activateCategory(data.activeIndex);
    });
    $scope.changeCategory = function(categoryIndex) {
      $scope.categoryFilter[$scope.categoryFilter.activeIndex].ngClass = '';
      activateCategory(categoryIndex);
    };
    
    $scope.event = {id:6};
  }]);
