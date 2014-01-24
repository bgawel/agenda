'use strict';

angular.module('frontendApp')
  .factory('Metadata', ['$resource', function($resource){
    return $resource('metadata/:what.json', {}, {
      week: {method:'GET', params:{what:'week'}, isArray:true}
    });
  }]);
