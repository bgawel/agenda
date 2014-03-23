'use strict';

angular.module('frontendApp')
  .directive('infoPanel', function () {
    return {
      restrict: 'E',
      templateUrl: 'template/infoPanel/infoPanel.html',
      scope: {
        panel: '=panel',
        form: '=form'
      }
    };
  });