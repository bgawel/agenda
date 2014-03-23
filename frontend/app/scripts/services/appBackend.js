'use strict';

var app = angular.module('frontendApp');

// TODO bgawel: make it configurable for tests
var serwerUrl = 'http://localhost:8080/';
//var serwerUrl = '';
var url = function(path) {
  return serwerUrl + path;
};

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

app.factory('Config', ['$http', function($http) {
  return {
    now : function() {
      return $http.get(url('b/config/now.json')).then(function(result) {
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
      return $http.get(url('b/evntProj/byDate/' + date + '.json'), {params : {category : category, inst: institution}}).
        then(function(result) {
          return result.data;
        });
    },
    byId : function(eventId) {
      return $http({method: 'GET', url: url('b/evntProj/byEvent/' + eventId + '.json')}).
        then(function(result) {
          return result.data;
        });
    },
    submitted : function(instId) {
      return $http.get(url('b/evntProj/submitted/' + instId + '.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Inst', ['$resource', function($resource) {
  return $resource(url('b/inst/:id.json'), null,
  {
    update: { method: 'PUT' }
  });
}]);

app.factory('Event', ['$resource', function($resource) {
  return $resource(url('b/event/:id.json'), null,
  {
    update: { method: 'PUT' }
  });
}]);

app.factory('Insts', ['$http', function($http) {
  return {
    names : function() {
      return $http.get(url('b/instProj/names.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Uploader', ['$fileUploader', '$http', function($fileUploader, $http) {
  return {
    create : function(scope) {
      return $fileUploader.create({
        scope: scope,
        url: url('b/upload/evntPic'),
        removeAfterUpload: false,
        headers: {
          'X-Auth-Token': $http.defaults.headers.common['X-Auth-Token']
        }
      });
    }
  };
}]);

app.factory('Auth', ['$rootScope', '$http', '$q', '$timeout', '$cookies', 
                     function($rootScope, $http, $q, $timeout, $cookies) {
  return {
    login : function(credentials, rememberMe) {
      return $http.post(url('b/rest/login.json'), credentials).then(function(result) {
        $http.defaults.headers.common['X-Auth-Token'] = result.data.token;
        if (rememberMe) {
          $cookies.username = result.data.username;
        } else {
          $cookies.username = '';
        }
        $rootScope.$broadcast('onAuthenticationSuccess', result.data);
        return result.data;
      });
    },
    check : function() {
      if (!$http.defaults.headers.common['X-Auth-Token']) {
        var deferred = $q.defer();
        $timeout(function(){ deferred.reject({status: 401}); }, 0);
        return deferred.promise;
      }
      return $http.get(url('b/rest/checkLogin'));
    },
    logout : function() {
      return $http.post(url('b/rest/logout')).then(function() {
        delete $http.defaults.headers.common['X-Auth-Token'];
        $rootScope.$broadcast('onLogoutSuccess');
        return true;
      });
    },
    resetPwd : function(username) {
      return $http.post(url('b/rest/resetPwd/' + username));
    },
    changePwd : function
  };
}]);