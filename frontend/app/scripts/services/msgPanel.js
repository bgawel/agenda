'use strict';

angular.module('frontendApp')
  .factory('MsgPanel', function() {
    return {
      show : function(panel, type, msg) {
        var messages = [];
        if (msg) {
          messages = angular.isArray(msg) ? msg : [msg];
        }
        panel.type = type;
        panel.messages = messages;
        panel.show = true;
      },
      showError : function(panel, messages) {
        this.show(panel, 'danger', messages);
      },
      showSuccess : function(panel, messages) {
        this.show(panel, 'success', messages);
      },
      hide : function(panel) {
        panel.messages = [];
        panel.show = false;
      },
      unexpectedError : function(panel, messages) {
        if (!messages) {
          messages = 'Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl';
        }
        this.showError(panel, messages);
      }
    };
  });
