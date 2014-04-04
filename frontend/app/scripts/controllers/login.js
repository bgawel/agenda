'use strict';

angular.module('frontendApp')
  .controller('LoginCtrl', ['$scope', '$routeParams', '$location', '$cookies', '$modal', 'Auth', 'MsgPanel',
                            'Progressbar',
                            function ($scope, $routeParams, $location, $cookies, $modal, Auth, MsgPanel, Progressbar) {
    $scope.form = {};
    $scope.loginMsgPanel = {show: false, messages:[]};
    $scope.username = $cookies.username;
    if ($scope.username) {
      $scope.rememberMe = true;
    }
    $scope.login = function() {
      $scope.formLoginValidations = true;
      if ($scope.form.login.$invalid) {
        return;
      }
      Progressbar.open();
      Auth.login({username: $scope.username, password: $scope.password}, $scope.rememberMe).then(
        function(data) {
          Progressbar.close();
          $location.url('panel/' + data.id);
          $location.search($routeParams);
        },
        function(httpResponse) {
          return httpResponse.status === 403 &&
            MsgPanel.showError($scope.loginMsgPanel, $scope.i18n.login.badCredentials, $scope.form.login);
        }
      );
    };
    $scope.resetPwd = function() {
      var modalInstance = $modal.open({
        templateUrl: 'views/resetPwd.html',
        controller: ResetPwdCtrl,
        scope: $scope,
        backdrop: false
      });
      modalInstance.result.then(function() {
        $scope.username = $scope.password = null;
        MsgPanel.showSuccess($scope.loginMsgPanel, $scope.i18n.login.resetPwd, $scope.form.inst);
      });
    };
    
    var ResetPwdCtrl = ['$scope', '$modalInstance', 'Auth', 'ServerError', 'Progressbar',
                        function ($scope, $modalInstance, Auth, ServerError, Progressbar) {
      $scope.pwdMsgPanel = {show: false, messages:[]};
      $scope.pwd = {username: null};
      $scope.reset = function() {
        $scope.formPwdValidations = true;
        if ($scope.form.pwd.$invalid) {
          return;
        }
        Progressbar.open();
        Auth.resetPwd({username: $scope.pwd.username}).then(
          function() {
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
    }];
  }]);
