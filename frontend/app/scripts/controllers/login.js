'use strict';

angular.module('frontendApp')
  .controller('LoginCtrl', ['$scope', '$routeParams', '$location', '$cookies', 'Auth', 'MsgPanel', 'Progressbar',
                            function ($scope, $routeParams, $location, $cookies, Auth, MsgPanel, Progressbar) {
    $scope.form = {};
    $scope.loginMsgPanel = {show: false, messages:[]};
    $scope.username = $cookies.username;
    if ($scope.username) {
      $scope.rememberMe = true;
    }
    var panelOption = $routeParams.o ? ('?o=' + $routeParams.o) : '';
    $scope.login = function() {
      $scope.formLoginValidations = true;
      if ($scope.form.login.$invalid) {
        return;
      }
      Progressbar.open();
      Auth.login({username: $scope.username, password: $scope.password}, $scope.rememberMe).then(
        function(data) {
          Progressbar.close();
          $location.url('panel/' + data.id + panelOption);
        },
        function(httpResponse) {
          httpResponse.status === 403 &&
            MsgPanel.showError($scope.loginMsgPanel, $scope.i18n.login.badCredentials, $scope.form.login)
        }
      );
    };
    $scope.resetPwd = function() {
      if ($scope.form.login.username.$invalid) {
        return;
      }
      Progressbar.open();
      Auth.resetPwd($scope.username).then(
          function() {
            MsgPanel.showSuccess($scope.loginMsgPanel, $scope.i18n.login.resetPwd, $scope.form.login);
            Progressbar.close();
          },
          function(httpResponse) {
            httpResponse.status === 422 &&
              MsgPanel.showError($scope.loginMsgPanel, $scope.i18n.login.pwdNotFound, $scope.form.login);
          }
       );
    };
  }]);
