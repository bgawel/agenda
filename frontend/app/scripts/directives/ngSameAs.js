'use strict';

angular.module('frontendApp')
  .directive('ngSameAs', ['$parse', function($parse) {
    return {
      require: 'ngModel',
      link: function(scope, elm, attrs, ctrl) {
        var getter = $parse(attrs.ngSameAs);
        ctrl.$parsers.push(function(viewValue) {
          if (ctrl.$isEmpty(viewValue) || viewValue === getter(scope)) {
            ctrl.$setValidity('sameAs', true);
            return viewValue;
          } else {
            ctrl.$setValidity('sameAs', false);
            return undefined;
          }
        });
      }
    };
  }]);