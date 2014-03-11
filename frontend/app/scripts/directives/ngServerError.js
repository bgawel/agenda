'use strict';

angular.module('frontendApp')
  .directive('ngServerError', function() {
    return {
      require: 'ngModel',
      link: function(scope, elm, attrs, ctrl) {
        var registerForFieldName = attrs.ngServerErrorName ? attrs.ngServerErrorName : ctrl.$name;
        scope.$on('serverErrorOccurred-' + registerForFieldName, function(event, errorText) {
          ctrl.$error.errorText = errorText;
          ctrl.$setValidity('server', false);
        });
        ctrl.$parsers.unshift(function(viewValue) {
          ctrl.$setValidity('server', true);
          return viewValue;
        });
      }
    };
  });