'use strict';

angular.module('frontendApp')
  .directive('ngDateFix', function () {
    return {
      restrict: 'EA',
      require: 'ngModel',
      link: function (scope, element, attrs, ctrl) {
        var format = attrs.datepickerPopup;
        ctrl.$parsers.push(function(viewValue) {
          var newDate = ctrl.$viewValue; // viewValue is null, why??
          // pass through if date from popup
          if (ctrl.$isEmpty(newDate) || typeof newDate !== 'string' || format !== 'dd-MM-yyyy') {
            return newDate;
          }
          var split = newDate.split('-');
          var day = parseInt(split[0]);
          var month = parseInt(split[1]) - 1;
          var year = parseInt(split[2]);
          var date = new Date();
          date.setFullYear(year, month, day);
          var maxDate = scope[attrs.max];
          var minDate = scope[attrs.min];
          if (isNaN(date.getTime()) ||
              // because 32-03 is to 01-04 but displayed as 32-03
              year !== date.getFullYear() || month !== date.getMonth() || day !== date.getDate() ||
              (maxDate && date > maxDate) ||
              (minDate && date < minDate)) {
            ctrl.$setValidity('date', false);
            date = undefined;
          } else {
            ctrl.$setValidity('date', true);
            ctrl.$setViewValue(date);
          }
          return date;
        });
      }
    };
  });