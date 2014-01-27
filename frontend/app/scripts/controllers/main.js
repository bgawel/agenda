'use strict';

angular.module('frontendApp')
  .controller('MainCtrl',  ['$scope', 'Metadata', 'Events', function($scope, Metadata, Events) {
    $scope.toggleRightPanel = function () {
      $scope.rightPanel = ($scope.rightPanel === 'activeRight') ? '' : 'activeRight';
    };
    $scope.toggleLeftPanel = function () {
      $scope.leftPanel = ($scope.leftPanel === 'activeLeft') ? '' : 'activeLeft';
    };
    
    var filterAllId = 'all';
    $scope.orderEventsBy = 'time';
    
    var activateCategory = function(categoryIndex) {
      var activeCategory = $scope.categories[categoryIndex];
      $scope.categories.active = activeCategory;
      activeCategory.ngClass = 'active';
      $scope.categories.filter = (activeCategory.id === filterAllId) ? undefined : activeCategory;
    }
    Metadata.categories(function(data) {
      $scope.categories = data.entries;
      activateCategory(data.activeIndex);
    });
    $scope.changeCategory = function(categoryIndex) {
      $scope.categories.active.ngClass = '';
      activateCategory(categoryIndex);
    };
    
    var activateEventsDay = function(dayIndex) {
      var activeDay = $scope.weekMenu[dayIndex];
      $scope.weekMenu.active = activeDay;
      activeDay.ngClass = 'active';
      Events.byDate(activeDay.id).then(function(data) {
        $scope.events = data.events;
        var activeDayCategories = data.badges.categories;
        angular.forEach($scope.categories, function(category, index) {
          category.badge = activeDayCategories[index].badge;
        });
      });
    };
    Metadata.week(function(data) {
      $scope.weekMenu = data.entries;
      activateEventsDay(data.activeIndex);
    });
    $scope.changeEventsDay = function(dayIndex) {
      $scope.weekMenu.active.ngClass = '';
      activateEventsDay(dayIndex);
    };
  }]);
