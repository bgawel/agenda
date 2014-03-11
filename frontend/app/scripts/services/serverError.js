'use strict';

angular.module('frontendApp')
  .factory('ServerError', ['MsgPanel', function(MsgPanel) {
    return {
      show : function(httpResponse, scope, form, msgPanel) {
        if (httpResponse.status === 422) {
          if (httpResponse.data.fields) {
            var fieldsErrors = httpResponse.data.fields;
            for (var fieldName in fieldsErrors) {
              scope.$broadcast('serverErrorOccurred-' + fieldName, fieldsErrors[fieldName]);
            }
          }
          if (httpResponse.data.global) {
            MsgPanel.showError(msgPanel, httpResponse.data.global, form);
          }
        } else if (httpResponse.status === 500) {
          MsgPanel.unexpectedError(msgPanel, form);
        }
      }
    };
  }]);
