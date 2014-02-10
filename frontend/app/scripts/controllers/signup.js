'use strict';

angular.module('frontendApp')
  .controller('SignupCtrl', ['$scope', '$location', 'Inst', 'MsgPanel',
                             function ($scope, $location, Inst, MsgPanel) {
    $scope.form = {};
    $scope.instMsgPanel = {show:false, messages:[]};
    $scope.inst = {};
    $scope.saveInst = function() {
      if ($scope.form.inst.$invalid) {
        return;
      }
      Inst.save({}, $scope.inst,
      function(value) {
        if (value.success) {
          $location.url('panel/' + value.id + '?o=2');
        } else {
          MsgPanel.showError($scope.instMsgPanel, value.messages);
        }
      }, function() {
        MsgPanel.unexpectedError($scope.instMsgPanel);
      });
    };
  }]);
