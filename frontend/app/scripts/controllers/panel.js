'use strict';

angular.module('frontendApp')
  .controller('PanelCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    
    $scope.loadPanelDetails = function() {
    	$scope.partial = 'panelDetails.html';
    }
    
    $scope.loadNewEvent = function() {
    	$scope.partial = 'panelEvent.html';
    	$scope.placeDate = ['1', '2'];
    	$scope.removePlaceDate = function() {
    		$scope.placeDate.pop();
    	}
    	$scope.isRemovePlaceDateShown = function() {
    		return $scope.placeDate.length > 1;
    	}
    	$scope.addPlaceDate = function() {
    		$scope.placeDate.push('3');
    	}
    }
    
    $scope.loadIssuedEvents = function() {
    	$scope.partial = 'panelIssuedEvents.html';
    }
    
    $scope.loadPanelDetails();
  });
