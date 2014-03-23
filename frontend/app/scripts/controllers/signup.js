'use strict';

angular.module('frontendApp')
  .controller('SignupCtrl', ['$scope', '$location', 'Inst', 'ServerError', 'Progressbar', 'Insts', 'MsgPanel',
                             function ($scope, $location, Inst, ServerError, Progressbar, Insts, MsgPanel) {
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
            MsgPanel.showSuccess($scope.instMsgPanel, value.message, $scope.form.inst);
          }
        }, function(httpResponse) {
          ServerError.show(httpResponse, $scope, $scope.form.inst, $scope.instMsgPanel);
        });
    };
  }]);
