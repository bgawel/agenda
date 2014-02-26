'use strict';

var app = angular.module('frontendApp');

var serwerUrl = 'http://localhost:8080/'
var url = function(path) {
  return serwerUrl + path;
}

app.factory('Menu', ['$http', function($http) {
  return {
    week : function() {
      return $http.get(url('b/menu/week.json')).then(function(result) {
        return result.data;
      });
    },
    categories : function() {
      return $http.get(url('b/menu/categories.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Category', ['$http', function($http) {
  return {
    all : function() {
      return $http.get(url('b/category/all.json')).then(function(result) {
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
      return $http.get(url('b/evntPres/byDate/' + date + '.json'), {params : {category : category, inst: institution}}).
        then(function(result) {
          return result.data;
      });
    },
    byId : function(eventId, error410) {
      return $http({method: 'GET', url: url('b/evntPres/byEvent/' + eventId + '.json')}).
        error(function(data, status, headers, config) {
          if (status === 410) {
            error410(data)
          }
        }).
        then(function(result) {
          return result.data;
        });
    },
    submitted : function(instId) {
      return $http.get(url('b/evntPres/submitted/' + instId + '.json')).then(function(result) {
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