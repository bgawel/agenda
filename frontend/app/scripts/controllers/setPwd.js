'use strict';

angular.module('frontendApp')
  .controller('SetPwdCtrl', ['$scope', '$routeParams', '$location', '$growl', 'Progressbar', 'Auth', 'ServerError',
                             function ($scope, $routeParams, $location, $growl, Progressbar, Auth, ServerError) {
    $scope.pwdMsgPanel = {show: false, messages:[]};
    $scope.pwd = {newPwd: null, rePwd: null, username: $routeParams.username};
    $scope.set = function() {
      $scope.formPwdValidations = true;
      if ($scope.form.pwd.$invalid) {
        return;
      }
      Progressbar.open();
      Auth.setPwd({pwd: $scope.pwd.newPwd, token: $routeParams.token}).then(
        function() {
          Progressbar.close();
          $location.url('/');
          $growl.box(null, $scope.i18n.login.pwdChanged, {
            class: 'success',
            sticky: true
          }).open();
        },
        function(httpResponse) {
          if (httpResponse.status === 410) {
            $location.url('rc/invalid');
          } else {
            ServerError.show(httpResponse, $scope, $scope.pwdMsgPanel);
          }
        }
      );
    };
  }]);
