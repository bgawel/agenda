'use strict';

angular.module('frontendApp')
  .directive('ngClearFix', function() {
    return {
      link: function(scope, elem, attrs) {
        scope.$watch(attrs.ngModel, function (value) {
          if (!value) {
            $(elem).val(null);
          }
        });
      }
    };
  });