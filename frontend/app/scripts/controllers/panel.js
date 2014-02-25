'use strict';

angular.module('frontendApp')
  .controller('PanelCtrl', ['$scope', '$routeParams', '$filter', 'Inst', 'Category', 'Events', 'Event', 'MsgPanel', 'Uploader',
              function ($scope, $routeParams,  $filter, Inst, Category, Events, Event, MsgPanel, Uploader) {

    $scope.loadInst = function() {
      $scope.partial = 'panelInst.html';
      $scope.instMsgPanel = {show:false, messages:[]};
      $scope.inst = Inst.get({id:$routeParams.instId}, function() {
        $scope.inst.repwd = $scope.inst.pwd;
      });
      $scope.saveInst = function() {
        if ($scope.form.inst.$invalid) {
          return;
        }
        Inst.save($scope.inst,  // instead of $scope.inst.$save that populates $scope.inst with response
        function(value) {
          if (value.success) {
            MsgPanel.showSuccess($scope.instMsgPanel, value.messages);
          } else {
            MsgPanel.showError($scope.instMsgPanel, value.messages);
          }
        }, function() {
          MsgPanel.unexpectedError($scope.instMsgPanel);
        });
      };
    };
    
    var loadEmptyEvent = function(formId) {
      $scope.partial = 'panelEvent.html';
      $scope.cancelClicked = undefined;
      $scope.event = {id: formId, selectedPic:null};
      $scope.evtMsgPanel = {show:false, messages:[]};
      $scope.oneTimeType = true;
      $scope.pdtpTypePartial = 'panelEventOneTime.html';
      $scope.isMeridian = false;
      $scope.minDate = new Date();
      $scope.dateOptions = {
          'year-format': '\'yy\'',
          'starting-day': 1,
          'show-weeks': false // doesn't work
        };
      Category.all().then(function(data) {
        $scope.categories = data;
      });
      
      var uploader = $scope.uploader = Uploader.create($scope, formId);
      // http://nervgh.github.io/pages/angular-file-upload/examples/image-preview/controllers.js
      uploader.filters.push(function(item /* {File|HTMLInputElement} */) {
        var type = uploader.isHTML5 ? item.type : '/' + item.value.slice(item.value.lastIndexOf('.') + 1);
        type = '|' + type.toLowerCase().slice(type.lastIndexOf('/') + 1) + '|';
        var isImg = $scope.event.isImg = '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        return isImg;
      });
      uploader.bind('afteraddingfile', function () {
        if (uploader.queue.length > 1) {
          uploader.removeFromQueue(0);  // currently only one picture supported
        }
        $scope.event.pic = null;
      });
      $scope.removeFromQueue = function(item) {
        uploader.removeFromQueue(item);
        $scope.event.selectedPic = null;
      };
      $scope.removeImage = function() {
        $scope.event.pic = null;
      };
      
      $scope.removePdtp = function(index) {
        $scope.event.pdtps.splice(index, 1);
      };
      $scope.isRemovePdtpShown = function(pdtp) {
        return !pdtp.readonly && $scope.event.pdtps.length > 1;
      };
      $scope.addPdtp = function(index) {
        var pdtp = {};
        if ($scope.event.pdtps.length > 0) {
          var previousPdtp = $scope.event.pdtps[index];
          pdtp.time = previousPdtp.time;
          pdtp.price = previousPdtp.price;
          pdtp.place = previousPdtp.place;
          pdtp.timeDescription = previousPdtp.timeDescription;
        }
        $scope.event.pdtps.splice(index+1, 0, pdtp);
      };
      
      $scope.openCalendar = function($event, pdtp) {
        $event.preventDefault();
        $event.stopPropagation();
        pdtp.opened = true;
      };
      
      var postEvent = function() {
        Event.save($scope.event,
            function(value) {
              if (value.success) {
                $scope.loadExistingEvent(value.id);
                MsgPanel.showSuccess($scope.evtMsgPanel, value.messages);
              } else {
                MsgPanel.showError($scope.evtMsgPanel, value.messages);
              }
            }, function() {
              MsgPanel.unexpectedError($scope.evtMsgPanel);
            });
      };
      var uploadItemsAndPostEvent = function() {
        uploader.uploadAll();
        var noError = true;
        uploader.bind('error', function (event, xhr, item, response) {
          noError = false;
          if (response.messages) {
            MsgPanel.showError($scope.evtMsgPanel, response.messages);
          } else {
            MsgPanel.unexpectedError($scope.evtMsgPanel, 'Nie można zapisać zdjęcia');
          }
        });
        uploader.bind('completeall', function () {
          if (noError) {
            postEvent();
          }
        });
      };
      $scope.saveEvent = function() {
        if ($scope.form.event.$invalid) {
          return;
        }
        if (uploader.getNotUploadedItems().length) {
          uploadItemsAndPostEvent();
        } else {
          postEvent();
        }
      };
      
      $scope.isOneTimeType = function(is) {
        $scope.event.oneTimeType = is
        $scope.pdtpTypePartial = is ? 'panelEventOneTime.html' : 'panelEventTmp.html'
        if (!is) {
          var pdtp
          for (var i=0; i<$scope.event.pdtps.length; ++i) {
            pdtp = $scope.event.pdtps[i];
            pdtp.fromDate = pdtp.toDate;
            pdtp.timeDescription = $filter('date')($scope.event.time, 'dd MMMM yyyy');
          }
        }
      } 
    };
    
    $scope.loadNewEvent = function() {
      var defaultTime = new Date();
      loadEmptyEvent(-(defaultTime.getTime()));
      defaultTime.setHours(19);
      defaultTime.setMinutes(0);
      $scope.event.pdtps = [{time: defaultTime}]; // does not work
    };
    
    $scope.loadSubmittedEvents = function() {
      $scope.partial = 'panelSubmittedEvents.html';
      Events.submitted($routeParams.instId).then(function(data) {
        $scope.submittedEvents = data;
      });
    };
    
    $scope.loadExistingEvent = function(eventId) {
      loadEmptyEvent(eventId);
      $scope.event = Event.get({id:eventId}, function() {
        $scope.eventTitle = $scope.event.title;
      });
      $scope.reloadSubmittedEvent = function() {
        $scope.loadExistingEvent($scope.event.id);
      };
      $scope.cancelClicked = function() {
        $scope.eventTitle = undefined;
        $scope.loadSubmittedEvents();
        $scope.option = 3;
      };
      $scope.option = 4;
    };
    
    $scope.init = function() {
      $scope.form = {};
      $scope.loadInst();
      $scope.option = parseInt($routeParams.o);
      if ($scope.option === 2) {
        $scope.loadNewEvent();
      } else if ($scope.option === 3) {
        $scope.loadSubmittedEvents();
      } else {
        $scope.option = 1;
      }
    };
    $scope.init();
  }]);
