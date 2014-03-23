'use strict';

angular.module('frontendApp')
  .controller('ChangePwdCtrl', ['$scope', '$routeParams', '$location', '$cookies', 'Auth', 'MsgPanel', 'Progressbar',
                            function ($scope, $routeParams, $location, $cookies, Auth, MsgPanel, Progressbar) {
    $scope.form = {};
    $scope.pwdMsgPanel = {show: false, messages:[]};
    $scope.change = function() {
      $scope.formPwdValidations = true;
      if ($scope.form.pwd.$invalid) {
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
  }]);
