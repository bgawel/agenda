'use strict';

angular.module('frontendApp')
  .controller('SignupCtrl', ['$scope', '$location', 'Inst', 'ServerError', 'Progressbar', 'Insts', '$growl',
                             function ($scope, $location, Inst, ServerError, Progressbar, Insts, $growl) {
    Insts.names().then(function(data) {
      $scope.registeredInsts = data;
    });
    $scope.form = {};
    $scope.instMsgPanel = {show: false, messages:[]};
    $scope.inst = {};
    $scope.saveInst = function() {
      $scope.formInstValidations = true;
      if ($scope.form.inst.$invalid) {
        return;
      }
      Progressbar.open();
      Inst.save({}, $scope.inst,
        function(value) {
          Progressbar.close();
          if (value.id) {
            $location.url('panel/' + value.id + '?o=2');
          } else {
            $location.url('/');
            $growl.box(null, value.message, {
              class: 'success',
              sticky: true
            }).open();
          }
        }, function(httpResponse) {
          ServerError.show(httpResponse, $scope, $scope.instMsgPanel, $scope.form.inst);
        });
    };
  }]);
