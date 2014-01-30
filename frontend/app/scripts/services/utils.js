'use strict';

angular.module('frontendApp').factory('Utils', function() {
  return {
    arrayOfObjectsToMapOfInt: function(collection, propertyOfEntryAsKey) {
      var map = {};
      angular.forEach(collection, function(entry) {
        map[entry[propertyOfEntryAsKey]] = 0;
      });
      return map;
    }
  };
});
