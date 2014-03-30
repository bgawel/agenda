'use strict';

angular.module('frontendApp')
  .controller('PanelCtrl', ['$scope', '$routeParams', '$filter', '$location', '$modal', 'Inst', 'Category', 'Events',
                            'Event', 'MsgPanel', 'Uploader', 'ServerError', 'Progressbar', 'ConfirmDialog', 'Config', 
                            'Auth',
              function ($scope, $routeParams,  $filter, $location, $modal, Inst, Category, Events, Event, MsgPanel, 
                  Uploader, ServerError, Progressbar, ConfirmDialog, Config, Auth) {
    $scope.logout = function() {
      Auth.logout();
      $location.url('login');
    };
    
    $scope.loadInst = function() {
      $scope.formInstValidations = false; // if this is not explicitly set, the errors blink for a moment 
      Progressbar.open();
      changeOption(1);
      $scope.partial = 'panelInst.html';
      $scope.instMsgPanel = {show: false, messages: []};
      $scope.inst = Inst.get({id:$routeParams.instId}, function() {
        instLoaded();
        Progressbar.close();
        $scope.formInstValidations = true;
      }, function() {
        $scope.logout();
      });
    };
    $scope.saveInst = function() {
      if ($scope.form.inst.$invalid) {
        return;
      }
      Progressbar.open();
      Inst.update({id: $scope.inst.id}, $scope.inst,
        function(value) {
          instLoaded(value);
          MsgPanel.showSuccess($scope.instMsgPanel, $scope.i18n.status.updated, $scope.form.inst);
          Progressbar.close();
        }, function(httpResponse) {
          ServerError.show(httpResponse, $scope, $scope.instMsgPanel, $scope.form.inst);
        });
    };
    $scope.deleteInst = function() {
      ConfirmDialog.confirmDelete($scope, $scope.i18n.inst.del.title, $scope.i18n.inst.del.query).result
        .then(function() {
          Progressbar.open();
          $scope.inst.$delete({id: $scope.inst.id}, function() {
              Progressbar.close();
              ConfirmDialog.confirmInfo($scope, $scope.i18n.inst.del.title, $scope.i18n.inst.del.confirm).result
                .then(function() {
                  $location.url('/');
                });
            }, function(httpResponse) {
              ServerError.show(httpResponse, $scope, $scope.instMsgPanel, $scope.form.inst);
            });
        });
    };
    $scope.changePwd = function() {
      var modalInstance = $modal.open({
        templateUrl: 'views/changePwd.html',
        controller: ChangePwdCtrl,
        resolve: {
          Auth: function() { return Auth; },
          ServerError: function() { return ServerError },
          Progressbar: function() { return Progressbar }
        },
        scope: $scope,
        backdrop: false
      });
      modalInstance.result.then(function() {
        MsgPanel.showSuccess($scope.instMsgPanel, $scope.i18n.login.pwdChanged, $scope.form.inst);
      });
    };
    function instLoaded(value) {
      if (value) {
        $scope.inst = value;
      }
      $scope.inst.repwd = $scope.inst.password;
    }
    
    $scope.loadNewEvent = function() {
      $scope.formEventValidations = false;
      changeOption(2);
      var defaultTime = new Date();
      defaultTime.setHours(19);
      defaultTime.setMinutes(0);
      $scope.event = {pdtps: [{startTime: defaultTime}], oneTimeType: true, canDelete: true,
          institution: {id: $scope.inst.id}};
      initEventPanel();
      $scope.cancelClicked = undefined;
    };
    
    $scope.loadSubmittedEvents = function() {
      changeOption(3);
      Progressbar.open();
      $scope.partial = 'panelSubmittedEvents.html';
      Events.submitted($routeParams.instId).then(function(data) {
        $scope.submittedEvents = data.events;
        Progressbar.close();
      });
    };
    
    $scope.loadExistingEvent = function(eventId) {
      $scope.formEventValidations = false;
      Progressbar.open();
      loadExistingEventView(eventId);
      $scope.event = Event.get({id: eventId}, function() {
        eventLoaded();
        Progressbar.close();
        $scope.formEventValidations = true;
      }, function() {
        $scope.loadSubmittedEvents();
      });
    };
    
    $scope.reloadSubmittedEvent = function() {
      $scope.loadExistingEvent($scope.event.id);
    };
    
    $scope.deleteEvent = function() {
      ConfirmDialog.confirmDelete($scope, $scope.i18n.event.del.title, $scope.i18n.event.del.query).result
      .then(function() {
        Progressbar.open();
        $scope.event.$delete({id: $scope.event.id}, function() {
            Progressbar.close();
            ConfirmDialog.confirmInfo($scope, $scope.i18n.event.del.title, $scope.i18n.event.del.confirm).result
              .then(function() {
                $scope.loadNewEvent();
              });
          }, function(httpResponse) {
            ServerError.show(httpResponse, $scope, $scope.evtMsgPanel, $scope.form.event);
          });
      });
    };
    
    $scope.init = function() {
      $scope.form = {};
      $scope.loadInst();
      var option = parseInt($routeParams.o);
      if (option === 2) {
        $scope.loadNewEvent();
      } else if (option === 3) {
        $scope.loadSubmittedEvents();
      } else if (option === 4 && $routeParams.e) {
        $scope.loadExistingEvent($routeParams.e);
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
      initEventPanel();
      $scope.cancelClicked = function() {
        $scope.eventTitle = undefined;
        $scope.loadSubmittedEvents();
      };
      changeOption(4, eventId);
    }
    function initEventPanel() {
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
      createUploader();
    }
    function loadCategories() {
      Category.all().then(function(data) {
        $scope.categories = data;
      });
    }
    function loadConfig() {
      Config.now().then(function(data) {
        $scope.minDate = data.now;
      });
    }
    function createUploader() {
      var uploader = $scope.uploader = Uploader.create($scope);
      // http://nervgh.github.io/pages/angular-file-upload/examples/image-preview/controllers.js
      uploader.filters.push(function(item /* {File|HTMLInputElement} */) {
        var type = uploader.isHTML5 ? item.type : '/' + item.value.slice(item.value.lastIndexOf('.') + 1);
        type = '|' + type.toLowerCase().slice(type.lastIndexOf('/') + 1) + '|';
        var isImg = !($scope.event.isNotImg = '|jpg|png|jpeg|bmp|gif|'.indexOf(type) < 0);
        return isImg;
      });
      uploader.filters.push(function(item) {
        var okSize = !($scope.event.notOkSize = item.size > 3145728); // <= 3 MB
        return okSize;
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
      Progressbar.open();
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
      uploader.bind('success', function (event, xhr, item, response) {
        $scope.event.picId = response.fileId;
      }); 
      uploader.bind('error', function (event, xhr, item, response) {
        noError = false;
      });
      uploader.bind('completeall', function () {
        if (noError) {
          saveOrUpdateEvent();
        } else {
          MsgPanel.showError($scope.evtMsgPanel, $scope.i18n.event.uploadError, $scope.form.event);
          Progressbar.close();
        }
      });
    }
    function saveOrUpdateEvent() {
      var errorCallback = function(httpResponse) {
        ServerError.show(httpResponse, $scope, $scope.evtMsgPanel, $scope.form.event);
      };
      var successCallback = function(value, successMsg) {
        eventLoaded(value);
        $scope.uploader.clearQueue();
        MsgPanel.showSuccess($scope.evtMsgPanel, successMsg, $scope.form.event);
        Progressbar.close();
      };
      var event = eventToTransfer();
      if (event.id) {
        Event.update({id: event.id, picId: $scope.event.picId}, event,
          function(value) {
            successCallback(value, $scope.i18n.status.updated);
          }, errorCallback);
      } else {
        Event.save({picId: $scope.event.picId}, event,
          function(value) {
            loadExistingEventView(value.id);
            successCallback(value, $scope.i18n.event.addedNew);
          }, errorCallback);
      }
    }
    function eventToTransfer() {
      var event = angular.copy($scope.event);
      angular.forEach(event.pdtps, function(pdtp) {
        // to prevent converting to UTC
        if (!pdtp.readonly) {
          pdtp.fromDate = dtToTransfer(pdtp.fromDate);
          pdtp.toDate = dtToTransfer(pdtp.toDate);
          pdtp.startTime = dtToTransfer(pdtp.startTime);
        }
      });
      return event;
    }
    function dtToTransfer(date) {
      if (typeof date === 'object') {
        return $filter('date')(date, 'yyyy-MM-ddTHH:mm');
      }
      return date;
    }
    
    $scope.isOneTimeType = function($event, isOneTimeType) {
      $event.preventDefault();
      $event.stopPropagation();
      if ($scope.event.id && $scope.event.oneTimeType !== isOneTimeType) {
        ConfirmDialog.confirmQuestion($scope, $scope.i18n.event.changeType.title, $scope.i18n.event.changeType.query).
          result.then(function () {
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
          pdtp.timeDescription = $filter('date')(pdtp.startTime, $scope.i18n.timeFormat);
        }
      }
    }
    
    var ChangePwdCtrl = function ($scope, $modalInstance, Auth, ServerError, Progressbar) {
      $scope.pwdMsgPanel = {show: false, messages:[]};
      $scope.pwd = {oldPwd: null, newPwd: null, rePwd: null}; // it has to go through 'pwd', otherwise not propagated to scope
      $scope.change = function() {
        $scope.formPwdValidations = true;
        if ($scope.form.pwd.$invalid) {
          return;
        }
        Progressbar.open();
        Auth.changePwd({pwd: $scope.pwd.oldPwd, newPwd: $scope.pwd.newPwd}).then(
          function(data) {
            Progressbar.close();
            $modalInstance.close();
          },
          function(httpResponse) {
            ServerError.show(httpResponse, $scope, $scope.pwdMsgPanel);
          }
        );
      };
      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };
    };
    
    function changeOption(option, eventId) {
      $scope.option = option;
      var params = {o: option};
      if (eventId) {
        params.e = eventId;
      }
      $location.search(params);
    }
  }]);
