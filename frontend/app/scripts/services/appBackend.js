'use strict';

var app = angular.module('frontendApp');

// TODO bgawel: make it configurable for tests
// local CORS config
//var serwerUrl = 'http://localhost:8080/b/';
// local tests, client development config
//var serwerUrl = 'b/';
// production config
var serwerUrl = '';

var url = function(path) {
  return serwerUrl + path;
};

var AUTH_TOKEN_NAME = 'X-XSRF-TOKEN';

app.factory('Menu', ['$http', function($http) {
  return {
    week : function() {
      return $http.get(url('menu/week.json')).then(function(result) {
        return result.data;
      });
    },
    categories : function() {
      return $http.get(url('menu/categories.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Config', ['$http', function($http) {
  return {
    now : function() {
      return $http.get(url('config/now.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Category', ['$http', function($http) {
  return {
    all : function() {
      return $http.get(url('category/all.json')).then(function(result) {
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
      return $http.get(url('evntProj/byDate/' + date + '.json'), {params : {category : category, inst: institution}}).
        then(function(result) {
          return result.data;
        });
    },
    byId : function(eventId) {
      return $http({method: 'GET', url: url('evntProj/byEvent/' + eventId + '.json')}).
        then(function(result) {
          return result.data;
        });
    },
    submitted : function(instId) {
      return $http.get(url('evntProj/submitted/' + instId + '.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Inst', ['$resource', function($resource) {
  return $resource(url('inst/:id.json'), null,
  {
    update: { method: 'PUT' }
  });
}]);

app.factory('Event', ['$resource', function($resource) {
  return $resource(url('event/:id.json'), null,
  {
    update: { method: 'PUT' }
  });
}]);

app.factory('Insts', ['$http', function($http) {
  return {
    names : function() {
      return $http.get(url('instProj/names.json')).then(function(result) {
        return result.data;
      });
    }
  };
}]);

app.factory('Uploader', ['$fileUploader', '$http', function($fileUploader, $http) {
  return {
    create : function(scope) {
      var headers = {};
      headers[AUTH_TOKEN_NAME] = $http.defaults.headers.common[AUTH_TOKEN_NAME];
      return $fileUploader.create({
        scope: scope,
        url: url('upload/evntPic'),
        removeAfterUpload: false,
        headers: headers
      });
    }
  };
}]);

app.factory('Auth', ['$rootScope', '$http', '$q', '$timeout', '$cookies', '$window',
                     function($rootScope, $http, $q, $timeout, $cookies, $window) {
  return {
    login : function(credentials, rememberMe) {
      return $http.post(url('rest/login.json'), credentials).then(function(result) {
        $cookies[AUTH_TOKEN_NAME] = $http.defaults.headers.common[AUTH_TOKEN_NAME] = result.data.token;
        if (rememberMe) {
          var username = result.data.username;
          var expiresDate = new Date();
          expiresDate.setTime(expiresDate.getTime() + (30*24*60*60*1000));
          var expires = 'expires=' + expiresDate.toGMTString();
          $window.document.cookie = 'username=' + username + '; ' + expires;
        } else {
          delete $cookies.username;
        }
        $rootScope.$broadcast('onAuthenticationSuccess', result.data);
        return result.data;
      });
    },
    assert : function() {
      var usedTokenFromCookie = false;
      var headers = $http.defaults.headers.common;
      if (!headers[AUTH_TOKEN_NAME]) {
        var token = $cookies[AUTH_TOKEN_NAME];
        if (token) {
          headers[AUTH_TOKEN_NAME] = token;
          usedTokenFromCookie = true;
        } else {
          var deferred = $q.defer();
          $timeout(function(){ deferred.reject({status: 666}); }, 0);
          return deferred.promise;
        }
      }
      return $http.get(url('rest/checkLogin')).then(function(result) {
        if (usedTokenFromCookie) {
          $rootScope.$broadcast('onAuthenticationSuccess', result.data);
        }
      });
    },
    checkIfLoggedIn : function() {
      var headers = $http.defaults.headers.common;
      if (!headers[AUTH_TOKEN_NAME]) {
        var token = $cookies[AUTH_TOKEN_NAME];
        if (token) {
          headers[AUTH_TOKEN_NAME] = token;
          return $http.get(url('rest/checkLogin')).then(function(result) {
            $rootScope.$broadcast('onAuthenticationSuccess', result.data);
          });
        }
      }
    },
    logout : function() {
      return $http.post(url('rest/logout')).then(function() {
        delete $cookies[AUTH_TOKEN_NAME];
        delete $http.defaults.headers.common[AUTH_TOKEN_NAME];
        $rootScope.$broadcast('onLogoutSuccess');
        return true;
      });
    },
    resetPwd : function(data) {
      return $http.post(url('rest/resetPwd.json'), data);
    },
    setPwd : function(data) {
      return $http.post(url('rest/setPwd.json'), data);
    },
    changePwd : function(data) {
      return $http.post(url('rest/changePwd.json'), data);
    }
  };
}]);