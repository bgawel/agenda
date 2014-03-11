'use strict';

angular.module('frontendApp')
  .controller('PanelCtrl', ['$scope', '$routeParams', '$filter', '$location', 'Inst', 'Category', 'Events', 'Event',
                            'MsgPanel', 'Uploader', 'ServerError', 'Progressbar', 'ConfirmDialog', 'Config',
              function ($scope, $routeParams,  $filter, $location, Inst, Category, Events, Event, MsgPanel, Uploader,
                  ServerError, Progressbar, ConfirmDialog, Config) {

    $scope.loadInst = function() {
      $scope.formInstValidations = false; // if this is not explicitly set, the errors blink for a moment 
      Progressbar.open($scope);
      $scope.partial = 'panelInst.html';
      $scope.instMsgPanel = {show: false, messages: []};
      $scope.inst = Inst.get({id:$routeParams.instId}, function() {
        instLoaded();
        Progressbar.close($scope);
        $scope.formInstValidations = true;
      });
    };
    $scope.saveInst = function() {
      if ($scope.form.inst.$invalid) {
        return;
      }
      Progressbar.open($scope);
      Inst.update({id: $scope.inst.id}, $scope.inst,
        function(value) {
          instLoaded(value);
          MsgPanel.showSuccess($scope.instMsgPanel, 'Dane zostały zaktualizowane', $scope.form.inst);
          Progressbar.close($scope);
        }, function(httpResponse) {
          ServerError.show(httpResponse, $scope, $scope.form.inst, $scope.instMsgPanel);
          Progressbar.close($scope);
        });
    };
    $scope.deleteInst = function() {
      ConfirmDialog.confirmDelete($scope, 'Usunięcie konta organizatora',
          'Czy na pewno chcesz usunąć konto razem z wszystkimi zgłoszonymi wydarzeniami?').result
        .then(function() {
          Progressbar.open($scope);
          $scope.inst.$delete({id: $scope.inst.id}, function() {
              Progressbar.close($scope);
              ConfirmDialog.confirmInfo($scope, 'Usunięcie konta organizatora',
                  'Konto zostało usunięte wraz z wszystkimi danymi.').result
                .then(function() {
                  $location.url('/');
                });
            }, function(httpResponse) {
              ServerError.show(httpResponse, $scope, $scope.form.inst, $scope.instMsgPanel);
              Progressbar.close($scope);
            });
        });
    };
    function instLoaded(value) {
      if (value) {
        $scope.inst = value;
      }
      $scope.inst.repwd = $scope.inst.password;
    }
    
    $scope.loadNewEvent = function() {
      var defaultTime = new Date();
      var newEventId = -defaultTime.getTime();
      defaultTime.setHours(19);
      defaultTime.setMinutes(0);
      $scope.event = {id: newEventId, pdtps: [{startTime: defaultTime}], oneTimeType: true, canDelete: true,
          institution: {id: $scope.inst.id}};
      initEventPanel($scope.event.id);
      $scope.cancelClicked = undefined;
    };
    
    $scope.loadSubmittedEvents = function() {
      Progressbar.open($scope);
      $scope.partial = 'panelSubmittedEvents.html';
      Events.submitted($routeParams.instId).then(function(data) {
        $scope.submittedEvents = data.events;
        Progressbar.close($scope);
      });
    };
    
    $scope.loadExistingEvent = function(eventId) {
      $scope.formEventValidations = false;
      Progressbar.open($scope);
      loadExistingEventView(eventId);
      $scope.event = Event.get({id:eventId}, function() {
        eventLoaded();
        Progressbar.close($scope);
        $scope.formEventValidations = true;
      });
    };
    
    $scope.reloadSubmittedEvent = function() {
      $scope.loadExistingEvent($scope.event.id);
    };
    
    $scope.deleteEvent = function() {
      ConfirmDialog.confirmDelete($scope, 'Usunięcie wydarzenia', 'Czy na pewno chcesz usunąć wydarzenie?').result
      .then(function() {
        Progressbar.open($scope);
        $scope.event.$delete({id: $scope.event.id}, function() {
            Progressbar.close($scope);
            ConfirmDialog.confirmInfo($scope, 'Usunięcie wydarzenia', 'Wydarzenie zostało usunięte.').result
              .then(function() {
                $scope.option = 2;
                $scope.loadNewEvent();
              });
          }, function(httpResponse) {
            ServerError.show(httpResponse, $scope, $scope.form.event, $scope.evtMsgPanel);
            Progressbar.close($scope);
          });
      });
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
    
    function eventLoaded(value) {
      if (value) {
        $scope.event = value;
      }
      $scope.eventTitle = $scope.event.title;
    }
    function loadExistingEventView(eventId) {
      initEventPanel(eventId);
      $scope.cancelClicked = function() {
        $scope.eventTitle = undefined;
        $scope.loadSubmittedEvents();
        $scope.option = 3;
      };
      $scope.option = 4;
    }
    function initEventPanel(eventId) {
      loadCategories();
      loadConfig();
      $scope.partial = 'panelEvent.html';
      $scope.evtMsgPanel = {show:false, messages:[]};
      $scope.isMeridian = false;
      $scope.dateOptions = {
          'year-format': '\'yy\'',
          'starting-day': 1,
          'show-weeks': false
        };
      createUploader(eventId);
    }
    function loadCategories() {
      Category.all().then(function(data) {
        $scope.categories = data;
      });
    }
    function loadConfig() {
      Config.now().then(function(data) {
        $scope.minDate = data.dateTime;
      });
    }
    function createUploader(eventId) {
      var uploader = $scope.uploader = Uploader.create($scope, eventId);
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
    }
    
    $scope.removeFromQueue = function(item) {
      $scope.uploader.removeFromQueue(item);
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
      var pdtp = {readonly: false};
      if ($scope.event.pdtps.length > 0) {
        var previousPdtp = $scope.event.pdtps[index];
        pdtp.startTime = previousPdtp.startTime;
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
    
    $scope.saveEvent = function() {
      $scope.formEventValidations = true;
      if ($scope.form.event.$invalid) {
        return;
      }
      Progressbar.open($scope);
      if ($scope.uploader.getNotUploadedItems().length) {
        uploadItemsAndSaveOrUpdateEvent();
      } else {
        saveOrUpdateEvent();
      }
    };
    function uploadItemsAndSaveOrUpdateEvent() {
      var uploader = $scope.uploader;
      uploader.uploadAll();
      var noError = true;
      uploader.bind('error', function (event, xhr, item, response) {
        noError = false;
        ServerError.show(response, $scope, $scope.form.event, $scope.evtMsgPanel);
      });
      uploader.bind('completeall', function () {
        if (noError) {
          saveOrUpdateEvent();
        } else {
          Progressbar.close($scope);
        }
      });
    }
    function saveOrUpdateEvent() {
      var errorCallback = function(httpResponse) {
        ServerError.show(httpResponse, $scope, $scope.form.event, $scope.evtMsgPanel);
        Progressbar.close($scope);
      };
      var successCallback = function(value, successMsg) {
        eventLoaded(value);
        MsgPanel.showSuccess($scope.evtMsgPanel, successMsg, $scope.form.event);
        Progressbar.close($scope);
      };
      var event = eventToTransfer();
      if (event.id < 0) {
        Event.save({}, event,
          function(value) {
            loadExistingEventView(value.id);
            successCallback(value, 'Nowe wydarzenie zostało dodane');
          }, errorCallback);
      } else {
        Event.update({id: event.id}, event,
          function(value) {
            successCallback(value, 'Dane zostały zaktualizowane');
          }, errorCallback);
      }
    }
    function eventToTransfer() {
      var event = angular.copy($scope.event);
      angular.forEach(event.pdtps, function(pdtp) {
        // to prevent converting to UTC
        if (!pdtp.readonly) {
          pdtp.fromDate = dateToTransfer(pdtp.fromDate);
          pdtp.toDate = dateToTransfer(pdtp.toDate);
          pdtp.startTime = timeToTransfer(pdtp.startTime);
        }
      });
      return event;
    }
    function dateToTransfer(date) {
      if (typeof date === 'object') {
        return $filter('date')(date, 'yyyy-MM-ddTHH:mm');
      }
      return date;
    }
    function timeToTransfer(date) {
      if (typeof date === 'object') {
        return $filter('date')(date, 'yyyy-MM-ddTHH:mm');
      }
      return date;
    }
    
    $scope.isOneTimeType = function($event, isOneTimeType) {
      $event.preventDefault();
      $event.stopPropagation();
      if ($scope.event.id > 0 && $scope.event.oneTimeType !== isOneTimeType) {
        ConfirmDialog.confirmQuestion($scope, 'Zmiana typu wydarzenia',
            'Czy na pewno chcesz zmienić typ wydarzenia? Część danych dotycząca terminów zostanie utracona.').result
        .then(function () {
          changeEventType(isOneTimeType);
        });
      } else {
        changeEventType(isOneTimeType);
      }
    };
    function changeEventType(isOneTimeType) {
      $scope.event.oneTimeType = isOneTimeType;
      if (!isOneTimeType) {
        var pdtp;
        for (var i=0; i<$scope.event.pdtps.length; ++i) {
          pdtp = $scope.event.pdtps[i];
          pdtp.fromDate = pdtp.toDate;
          pdtp.timeDescription = $filter('date')(pdtp.startTime, 'HH:mm');
        }
      }
    }
  }]);
