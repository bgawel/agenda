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
          form && form.$setPristine && form.$setPristine();
          $window.scrollTo(0,0);
        } else {
          panel.show = false;
        }
        return panel.show;
      },
      showError : function(panel, messages, form) {
        return this.show(panel, 'danger', messages, form);
      },
      showSuccess : function(panel, messages, form) {
        return this.show(panel, 'success', messages, form);
      },
      hide : function(panel) {
        panel.messages = [];
        panel.show = false;
      }
    };
  }]);
