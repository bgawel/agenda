'use strict';

angular.module('frontendApp')
  .controller('SignupCtrl', ['$scope', '$location', 'Inst', 'ServerError', 'Progressbar', 'Insts',
                             function ($scope, $location, Inst, ServerError, Progressbar, Insts) {
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
      Progressbar.open($scope);
      Inst.save({}, $scope.inst,
        function(value) {
          Progressbar.close($scope);
          $location.url('panel/' + value.id + '?o=2');
        }, function(httpResponse) {
          ServerError.show(httpResponse, $scope, $scope.form.inst, $scope.instMsgPanel);
          Progressbar.close($scope);
        });
    };
  }]);
