'use strict';

var app = angular.module('frontendApp');

app.factory('Metadata', ['$http', function($http) {
  return {
    weekMenu : function() {
      return $http.get('metadata/weekMenu.json').then(function(result) {
        return result.data;
      });
    },
    categoriesMenu : function() {
      return $http.get('metadata/categoriesMenu.json').then(function(result) {
        return result.data;
      });
    },
    categories : function() {
      return $http.get('metadata/categories.json').then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Events', ['$http', function($http) {
  return {
    byDate : function(date, category, institution) {
      // since $http.get returns a promise,
      // and promise.then() also returns a promise
      // that resolves to whatever value is returned in it's
      // callback argument, we can return that.
      return $http.get('events/filter/' + date + '.json', {
        params : {
          category : category,
          who: institution
        }
      }).then(function(result) {
        return result.data;
      });
    },
    byId : function(eventId) {
      return $http.get('events/id/' + eventId + '.json').then(function(result) {
        return result.data;
      });
    },
    issued : function(instId) {
      return $http.get('events/archv/' + instId + '.json').then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Inst', ['$resource', function($resource) {
  return $resource('inst/:id.json');
}]);
app.factory('Event', ['$resource', function($resource) {
  return $resource('event/:id.json');
}]);

app.factory('Uploader', ['$fileUploader', function($fileUploader) {
  return {
    create : function(scope, formId) {
      return $fileUploader.create({
        scope: scope,
        url: 'upload',
        formData: [{formId: formId}],
        removeAfterUpload: false
      });
    }
  };
}]);