'use strict';

var app = angular.module('frontendApp');

app.factory('Metadata', [ '$resource', function($resource) {
  return $resource('metadata/:what.json', {}, {
    week : {
      method : 'GET',
      params : {
        what : 'week'
      }
    },
    categories : {
      method : 'GET',
      params : {
        what : 'categories'
      }
    }
  });
} ]);

app.factory('Events', function($http) {
  return {
    byDate : function(dateId) {
      // since $http.get returns a promise,
      // and promise.then() also returns a promise
      // that resolves to whatever value is returned in it's
      // callback argument, we can return that.
      return $http.get('events/filter.json', {
        params : {
          date : dateId
        }
      }).then(function(result) {
        return result.data;
      });
    }
  };
});
