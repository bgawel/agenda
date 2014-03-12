'use strict';

angular.module('frontendApp')
  .factory('MsgPanel', ['$window', function($window) {
    return {
      show : function(panel, type, msg, form) {
        var messages = [];
        if (msg) {
          messages = angular.isArray(msg) ? msg : [msg];
        }
        if (messages.length) {
          panel.type = type;
          panel.messages = messages;
          panel.show = true;
          if (form && form.$setPristine) {
            form.$setPristine();
          }
          $window.scrollTo(0,0);
        }
      },
      showError : function(panel, messages, form) {
        this.show(panel, 'danger', messages, form);
      },
      showSuccess : function(panel, messages, form) {
        this.show(panel, 'success', messages, form);
      },
      hide : function(panel) {
        panel.messages = [];
        panel.show = false;
      },
      unexpectedError : function(panel, messages, form) {
        if (!messages) {
          messages = 'Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl';
        }
        this.showError(panel, messages, form);
      }
    };
  }]);
