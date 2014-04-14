'use strict';

angular.module('frontendApp')
  .controller('MainCtrl',
      ['$scope', '$location', '$anchorScroll', '$q', '$filter', '$cookies', '$timeout', '$window', '$rootScope',
       '$routeParams', 'Menu', 'Events', 'Progressbar', 'Auth',
      function($scope, $location, $anchorScroll, $q, $filter, $cookies,  $timeout, $window, $rootScope,
          $routeParams, Menu, Events, Progressbar, Auth) {
    $scope.toggleRightPanel = function () {
      $scope.rightPanel = ($scope.rightPanel === 'activeRight') ? '' : 'activeRight';
    };
    $scope.toggleLeftPanel = function () {
      $scope.leftPanel = ($scope.leftPanel === 'activeLeft') ? '' : 'activeLeft';
    };
    
    var DISPLAY_ALL_ID = 'all';
    var CAL_TYPE = 'cal';
    
    $scope.showMaxChars = 200;
        
    var initializeOrder = function() {
      var value = $routeParams.s;
      if (!value) {
        value = 'dateTime';
      }
      $scope.changeOrder(value);
    };
    var initializeCategoryIndex = function(data) {
      var index;
      var id = $routeParams.c;
      if (id) {
        index = findIndex($scope.categories, 'id', id);
      }
      return index >= 0 ? index : data.activeIndex;
    };
    var initializeWeekMenuIndexAndCalendar = function(data) {
      $scope.weekMenu.calendar = {
          opened: false,
          dateOptions:{
            'year-format': '\'yy\'',
            'starting-day': 1,
            'show-weeks': false
          }
        };
      var index;
      var calendarValue = $routeParams.cal;
      if (calendarValue) {
        $scope.weekMenu.calendar.value = calendarValue;
        index = findIndex($scope.weekMenu, 'type', CAL_TYPE);
      } else {
        var id = $routeParams.d;
        if (id) {
          index = findIndex($scope.weekMenu, 'type', id);
          if (index === undefined) {
            index = findIndex($scope.weekMenu, 'id', id);
            if (index === undefined) {
              var date = parseDate(id);
              if (date) {
                $scope.weekMenu.calendar.value = date;
                index = findIndex($scope.weekMenu, 'type', CAL_TYPE);
              }
            }
          }
        }
      }
      return index >= 0 ? index : data.activeIndex;
    };
    
    $scope.changeCategory = function(categoryIndex) {
      var activeCategory = activateCategory(categoryIndex);
      setCategoryFilter(activeCategory);
      setInstitutionOfGivenCategoryToAll(activeCategory);
    };
    var activateCategory = function(categoryIndex) {
      if ($scope.categories.active) {
        $scope.categories.active.ngClass = '';
      }
      var activeCategory = $scope.categories[categoryIndex];
      activeCategory.ngClass = 'active';
      activeCategory.index = categoryIndex;
      $scope.categories.active = activeCategory;
      appendSearchParam('c', activeCategory.id);
      return activeCategory;
    };
    var setCategoryFilter = function(category) {
      $scope.categories.filter = (category.id !== DISPLAY_ALL_ID) ? category : undefined;
    };
    var setInstitutionOfGivenCategoryToAll = function(category) {
      if ($scope.institutions[category.id]) {
        var index = findIndexOfAllForGivenCategory(category);
        if (index >= 0) {
          $scope.changeInstitution(index);
        } else {
          console.assert(false, 'Cannot find index of \'all\' institutions. The first institution was set');
          $scope.changeInstitution(0);
        }
      }
    };
    var findIndexOfAllForGivenCategory = function(category) {
      return findIndex($scope.institutions[category.id], 'id', DISPLAY_ALL_ID);
    };
    $scope.changeInstitution = function(institutionIndex) {
      setInstitutionFilter(activateInstitution(institutionIndex));
    };
    var activateInstitution = function(institutionIndex) {
      if ($scope.institutions.active) {
        $scope.institutions.active.ngClass = '';
      }
      var activeInstitution = $scope.institutions[$scope.categories.active.id][institutionIndex];
      activeInstitution.ngClass = 'active';
      $scope.institutions.active = activeInstitution;
      appendSearchParam('i', activeInstitution.id);
      return activeInstitution;
    };
    var setInstitutionFilter = function(institution) {
      $scope.institutions.filter = (institution.id !== DISPLAY_ALL_ID) ? institution : undefined;
    };
    
    $scope.changeEventsDay = function(dayIndex) {
      Progressbar.open();
      var activeDay = activateEventsDay(dayIndex);
      var activeCategory = $scope.categories.active;
      var activeInstitution = getActiveInstitution();
      $scope.events = [];
      $scope.institutions = {};
      fetchEventsByDay(activeDay, activeCategory, activeInstitution);
    };
    var activateEventsDay = function(dayIndex) {
      if ($scope.weekMenu.active) {
        $scope.weekMenu.active.ngClass = '';
      }
      var activeDay = $scope.weekMenu[dayIndex];
      activeDay.ngClass = 'active';
      activeDay.index = dayIndex;
      if (activeDay.type === CAL_TYPE) {
        activeDay.id = $filter('date')($scope.weekMenu.calendar.value, 'yyyy-MM-dd');
        appendSearchParam('cal', activeDay.id);
        appendSearchParam('d', null);
      } else {
        $scope.weekMenu.calendar.value = null;
        appendSearchParam('d', activeDay.type ? activeDay.type : activeDay.id);
        appendSearchParam('cal', null);
      }
      $scope.weekMenu.active = activeDay;
      return activeDay;
    };
    var getActiveInstitution = function() {
      var activeInstitution, activeInstitutionId;
      if ($scope.institutions.active) {
        activeInstitution = $scope.institutions.active;
        activeInstitutionId = activeInstitution.id;
      } else {
        activeInstitutionId = $routeParams.i;
      }
      return {value: activeInstitution, id: activeInstitutionId};
    };
    var fetchEventsByDay = function(day, category, institution) {
      Events.byDate(day.id, category.id, institution.id).then(function(data) {
        fillBadgesAndInstitutionsOfCategories(data.categories);
        restoreActiveInstitutionOfCategory(institution.value, category);;
        $scope.weekMenu.calendar.minDate = data.now;
        $scope.events = data.events;
        $scope.noEventsMsg = !$scope.events.length;
        $scope.newest = data.newest;
        $scope.soon = data.soon
        Progressbar.close();
        scrollToEventIfNeeded();
      });
    };
    var restoreActiveInstitutionOfCategory = function(institution, category) {
      var wasSet;
      if (institution) {
        wasSet = tryToSetInstitutionOfGivenCategory(category, institution.id) ||
          insertInstitutionAlthoughNotFound(institution, category);
      } else {
        var index = $routeParams.i;
        wasSet = index && tryToSetInstitutionOfGivenCategory(category, index);
      }
      return !wasSet && setInstitutionOfGivenCategoryToAll(category);
    };
    var tryToSetInstitutionOfGivenCategory = function(category, instId) {
      var index = findIndex($scope.institutions[category.id], 'id', instId);
      var found = index >= 0;
      if (found) {
        $scope.changeInstitution(index);
      }
      return found;
    };
    var insertInstitutionAlthoughNotFound = function(institution, category) {
      institution.badge = 0;
      insertInstitutionOfGivenCategoryAfterAll(institution, category);
      $scope.institutions.active = institution;
      return true;
    };
    var insertInstitutionOfGivenCategoryAfterAll = function(institution, category) {
      var index = findIndexOfAllForGivenCategory(category);
      $scope.institutions[category.id].splice(index+1, 0, institution);
    };
    var fillBadgesAndInstitutionsOfCategories = function(badgesInstitutions) {
      angular.forEach($scope.categories, function(category, index) {
        category.badge = badgesInstitutions[index].badge;
        $scope.institutions[category.id] = badgesInstitutions[index].who;
      });
    };
    
    $scope.openEventsCalendar = function($event, dayIndex) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.weekMenu.calendar.dayIndex = dayIndex;
      $scope.weekMenu.calendar.opened = true;
    };
    $scope.$watch('weekMenu.calendar.value', function(newVal, oldVal) {
      if (newVal !== oldVal && newVal && $scope.weekMenu.calendar.dayIndex !== undefined) {
        if (oldVal) {
          if (newVal.getTime() !== oldVal.getTime()) {
            $scope.changeEventsDay($scope.weekMenu.calendar.dayIndex);
          }
        } else {
          $scope.changeEventsDay($scope.weekMenu.calendar.dayIndex);
        }
      }
    });
    
    $scope.changeOrder = function(property) {
      $scope.orderEventsBy = property;
      appendSearchParam('s', property);
    };
    
    $scope.displayEvent = function(eventId, idPrefix) {
      $scope.saveFilters();
      $cookies.lastEvent = idPrefix + eventId;
      $location.url('event/' + eventId);
    };
    $scope.reload = function() {
      $location.search({});
      $timeout(function() { $window.location.reload(); }, 0); // $route.reload() causes problems in unit tests (unexpected GET views.main.html)
    };
    $scope.panel = function(option) {
      $scope.saveFilters();
      $location.url('panel/' + $rootScope.userId);
      $location.search({o: option});
    };
    $scope.about = function() {
      $scope.saveFilters();
      $location.url('about');
    };
    $scope.logout = function() {
      Auth.logout();
    };
    $scope.saveFilters = function() {
      $cookies.search = angular.toJson($location.search());
    };
    function restoreFilters() {
      if ($location.url() === '/') {
        var filters = $cookies.search;
        if (filters) {
          $routeParams = angular.fromJson(filters);
        }
      }
      delete $cookies.search;
    }
    
    var scrollTo = function(hash) {
      $timeout(function() {
        var old = $location.hash();
        $location.hash(hash);
        $anchorScroll();
        $location.hash(old);  //reset to old to keep any additional routing logic from kicking in, otherwise it renders twice
      }, 50);
    };
    var scrollToEventIfNeeded = function() {
      var eventId = $cookies.lastEvent;
      if (eventId) {
        delete $cookies.lastEvent;
        scrollTo(eventId);
      }
    };
    
    function appendSearchParam(paramName, value) {
      var params = $location.search();
      if (value) {
        params[paramName] = value;
      } else {
        delete params[paramName];
      }
      $location.search(params);
    }
    function findIndex(array, property, value) {
      for (var i = 0; i < array.length; ++i) {
        if (array[i][property] == value) {  // yes, == instead of ===
          return i;
        }
      }
    }
    function parseDate(dateAsString) {
      var split = dateAsString.split('-');
      if (split.length === 3) {
        var year = parseInt(split[0]);
        var month = parseInt(split[1]) - 1;
        var day = parseInt(split[2]);
        var date = new Date();
        date.setFullYear(year, month, day);
        if (!isNaN(date.getTime())) {
          return date;
        }
      }
    }
    
    $scope.init = function() {
      Auth.checkIfLoggedIn();
      restoreFilters();
      initializeOrder();
      $scope.institutions = {};
      $q.all([Menu.categories(), Menu.week()]).then(function(results) {
        $scope.categories = results[0].entries;
        $scope.changeCategory(initializeCategoryIndex(results[0]));
        $scope.weekMenu = results[1].entries;
        $scope.changeEventsDay(initializeWeekMenuIndexAndCalendar(results[1]));
      });
    };
    $scope.init();
    
  }]);
