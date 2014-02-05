'use strict';

angular.module('frontendApp')
  .controller('PanelCtrl', ['$scope', '$routeParams', 'Inst', 'Metadata', 'Events', 'Event',
                            function ($scope, $routeParams, Inst, Metadata, Events, Event) {
    var showPanel = function(panel, type, messages) {
      panel.type = type;
      panel.messages = messages;
      panel.show = true;
    };
    var showError = function(panel, messages) {
      showPanel(panel, 'danger', messages);
    };
    var showSuccess = function(panel, messages) {
      showPanel(panel, 'success', messages);
    };
    var hidePanel = function(panel) {
      panel.messages = [];
      panel.show = false;
    };
    var unexpectedError = function(panel) {
      showError(panel, ['Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie']);
    };
    
    $scope.loadInst = function() {
      $scope.partial = 'panelInst.html';
      $scope.instMsgPanel = {show:false, messages:[]};
      $scope.inst = Inst.get({id:$routeParams.instId}, function() {
        $scope.inst.repwd = $scope.inst.pwd;
      });
      $scope.saveInst = function() {
        $scope.inst.$save({},
        function(value) {
          if (value.success) {
            showSuccess($scope.instMsgPanel, value.messages);
          } else {
            showError($scope.instMsgPanel, value.messages);
          }
        }, function() {
          unexpectedError($scope.instMsgPanel);
        });
      };
    };
    
    var loadEmptyEvent = function() {
      $scope.partial = 'panelEvent.html';
      $scope.cancelClicked = undefined;
      $scope.event = {};
      $scope.evtMsgPanel = {show:false, messages:[]};
      $scope.isMeridian = false;
      $scope.minDate = new Date();
      $scope.dateOptions = {
          'year-format': '\'yy\'',
          'starting-day': 1,
          'show-weeks': false // doesn't work
        };
      Metadata.categories().then(function(data) {
        $scope.categories = data;
      });
      $scope.removePdp = function(index) {
        $scope.event.pdp.splice(index, 1);
      };
      $scope.isRemovePdpShown = function(pdp) {
        return !pdp.readonly && $scope.event.pdp.length > 1;
      };
      $scope.addPdp = function(index) {
        var pdp = {};
        if ($scope.event.pdp.length > 0) {
          var previousPdp = $scope.event.pdp[index];
          pdp.time = previousPdp.time;
          pdp.price = previousPdp.price;
          pdp.place = previousPdp.place;
        }
        $scope.event.pdp.splice(index+1, 0, pdp);
      };
      $scope.openCalendar = function($event, pdp) {
        $event.preventDefault();
        $event.stopPropagation();
        pdp.opened = true;
      };
      $scope.saveEvent = function() {
        console.log($scope.event);
        Inst.save({}, $scope.event,
        function(value) {
          if (value.success) {
            showSuccess($scope.evtMsgPanel, value.messages);
          } else {
            showError($scope.evtMsgPanel, value.messages);
          }
        }, function() {
          unexpectedError($scope.evtMsgPanel);
        });
      };
    };
    
    $scope.loadNewEvent = function() {
      loadEmptyEvent();
      $scope.event = {pdp:[{time: new Date()}]};
    };
    
    $scope.loadIssuedEvents = function() {
      $scope.partial = 'panelIssuedEvents.html';
      Events.issued($routeParams.instId).then(function(data) {
        $scope.issuedEvents = data;
      });
      $scope.loadExistingEvent = function(index) {
        loadEmptyEvent();
        $scope.event = Event.get({id:$scope.issuedEvents[index].id});
        $scope.cancelClicked = function() {
          $scope.loadIssuedEvents();
        };
      };
    };
    
    $scope.loadInst();
  }]);
