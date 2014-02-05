'use strict';

angular.module('frontendApp')
  .controller('MainCtrl',
      ['$scope', '$location', '$anchorScroll', '$q', '$filter', '$cacheFactory', '$timeout', 'Metadata', 'Events',
      function($scope, $location, $anchorScroll, $q, $filter, $cacheFactory,  $timeout, Metadata, Events) {
    $scope.toggleRightPanel = function () {
      $scope.rightPanel = ($scope.rightPanel === 'activeRight') ? '' : 'activeRight';
    };
    $scope.toggleLeftPanel = function () {
      $scope.leftPanel = ($scope.leftPanel === 'activeLeft') ? '' : 'activeLeft';
    };
    
    var DISPLAY_ALL_ID = 'all';
    var DATE_ABBR = 'date';
    
    var CACHE_NAME = 'mainCache';
    var CACHE_DAY_KEY = 'mainCache_DAY';
    var CACHE_CATEGORY_KEY = 'mainCache_CATEGORY';
    var CACHE_INST_KEY = 'mainCache_INST';
    var CACHE_CALENDAR_KEY = 'mainCache_CALENDAR';
    var CACHE_ORDER_KEY = 'mainCache_ORDER';
    var CACHE_EVENT_KEY = 'mainCache_EVENT';
    var getCache = function() {
      var cache = $cacheFactory.get(CACHE_NAME);
      if (cache === undefined) {
        cache = $cacheFactory(CACHE_NAME);
      }
      return cache;
    };
    var initializeInstitutions = function() {
      return {active: getCache().get(CACHE_INST_KEY)};
    };
    var initializeOrder = function() {
      var value = getCache().get(CACHE_ORDER_KEY);
      if (value === undefined) {
        value = 'dateTime';
      }
      return value;
    };
    var initializeCategoryIndex = function(data) {
      var index = getCache().get(CACHE_CATEGORY_KEY);
      if (index === undefined) {
        index = data.activeIndex;
      }
      return index;
    };
    var initializeWeekMenuIndexAndCalendar = function(data) {
      var cache = getCache();
      var index = cache.get(CACHE_DAY_KEY);
      if (index === undefined) {
        index = data.activeIndex;
      }
      $scope.weekMenu.calendar = {
          minDate: new Date(),
          opened: false,
          dateOptions:{
            'year-format': '\'yy\'',
            'starting-day': 1,
            'show-weeks': false // doesn't work
          }
        };
      var calendarValue = cache.get(CACHE_CALENDAR_KEY);
      if (calendarValue !== undefined && calendarValue !== null) {
        $scope.weekMenu.calendar.value = calendarValue; // no trigger because dayIndex is undefined
      }
      return index;
    };
    
    $scope.init = function() {
      $scope.institutions = initializeInstitutions();
      $scope.orderEventsBy = initializeOrder();
      $q.all([Metadata.categoriesMenu(), Metadata.weekMenu()]).then(function(results) {
        $scope.categories = results[0].entries;
        $scope.changeCategory(initializeCategoryIndex(results[0]));
        $scope.weekMenu = results[1].entries;
        $scope.changeEventsDay(initializeWeekMenuIndexAndCalendar(results[1]));
      });
    };
    $scope.init();
    
    $scope.changeCategory = function(categoryIndex) {
      var activeCategory = activateCategory(categoryIndex);
      setCategoryFilter(activeCategory);
      setInstitutionOfGivenCategoryToAll(activeCategory);// tu jest bug jak init z cache
    };
    var activateCategory = function(categoryIndex) {
      if ($scope.categories.active !== undefined) {
        $scope.categories.active.ngClass = '';
      }
      var activeCategory = $scope.categories[categoryIndex];
      activeCategory.ngClass = 'active';
      activeCategory.index = categoryIndex;
      $scope.categories.active = activeCategory;
      return activeCategory;
    };
    var setCategoryFilter = function(category) {
      $scope.categories.filter = (category.id !== DISPLAY_ALL_ID) ? category : undefined;
    };
    var setInstitutionOfGivenCategoryToAll = function(category) {
      if ($scope.institutions[category.name] !== undefined) {
        var index = findIndexOfAllForGivenCategory(category);
        if (index !== null) {
          $scope.changeInstitution(index);
        } else {
          console.assert(index !== null, 'Cannot find index of \'all\' institutions. The first institution was set');
          $scope.changeInstitution(0);
        }
      }
    };
    var findIndexOfAllForGivenCategory = function(category) {
      var categoryInstitutions = $scope.institutions[category.name];
      for (var i = 0; i < categoryInstitutions.length; ++i) {
        if (categoryInstitutions[i].id === DISPLAY_ALL_ID) {
          return i;
        }
      }
      return null;
    };
    $scope.changeInstitution = function(institutionIndex) {
      setInstitutionFilter(activateInstitution(institutionIndex));
    };
    var activateInstitution = function(institutionIndex) {
      if ($scope.institutions.active !== undefined) {
        $scope.institutions.active.ngClass = '';
      }
      var activeInstitution = $scope.institutions[$scope.categories.active.name][institutionIndex];
      activeInstitution.ngClass = 'active';
      $scope.institutions.active = activeInstitution;
      return activeInstitution;
    };
    var setInstitutionFilter = function(institution) {
      $scope.institutions.filter = (institution.id !== DISPLAY_ALL_ID) ? institution : undefined;
    };
    
    $scope.changeEventsDay = function(dayIndex) {
      var activeDay = activateEventsDay(dayIndex);
      var activeCategory = $scope.categories.active;
      var activeInstitution = getActiveInstitution();
      $scope.events = [];
      $scope.institutions = {};
      fetchEventsByDay(activeDay, activeCategory, activeInstitution);
      scrollToEventIfNeeded();
    };
    var activateEventsDay = function(dayIndex) {
      if ($scope.weekMenu.active !== undefined) {
        $scope.weekMenu.active.ngClass = '';
      }
      var activeDay = $scope.weekMenu[dayIndex];
      activeDay.ngClass = 'active';
      activeDay.index = dayIndex;
      if (activeDay.abbr === DATE_ABBR) {
        activeDay.id = $filter('date')($scope.weekMenu.calendar.value, 'dd-MM-yyyy');
      } else {
        $scope.weekMenu.calendar.value = null;
      }
      $scope.weekMenu.active = activeDay;
      return activeDay;
    };
    var getActiveInstitution = function() {
      var activeInstitution, activeInstitutionId;
      if ($scope.institutions.active !== undefined) {
        activeInstitution = $scope.institutions.active;
        activeInstitutionId = activeInstitution.id;
      }
      return {value: activeInstitution, id: activeInstitutionId};
    };
    var fetchEventsByDay = function(day, category, institution) {
      Events.byDate(day.id, category.id, institution.id).then(function(data) {
        $scope.events = data.events;
        fillBadgesAndInstitutionsOfCategories(data.categories);
        restoreActiveInstitutionOfCategory(institution.value, category);
        fillNewestEventsIfPresent(data);
        fillComingSoonEventsIfPresent(data);
      });
    };
    var restoreActiveInstitutionOfCategory = function(institution, category) {
      if (institution !== undefined) {
        var wasSet = tryToSetInstitutionOfGivenCategoryToName(category, institution.name);
        if (!wasSet) {
          insertInstitutionAlthoughNotFound(institution, category);
        }
      } else {
        setInstitutionOfGivenCategoryToAll(category);
      }
    };
    var tryToSetInstitutionOfGivenCategoryToName = function(category, name) {
      var categoryInstitutions = $scope.institutions[category.name];
      for (var i = 0; i < categoryInstitutions.length; ++i) {
        if (categoryInstitutions[i].name === name) {
          $scope.changeInstitution(i);
          return true;
        }
      }
      return false;
    };
    var insertInstitutionAlthoughNotFound = function(institution, category) {
      institution.badge = 0;
      insertInstitutionOfGivenCategoryAfterAll(institution, category);
      $scope.institutions.active = institution;
    };
    var insertInstitutionOfGivenCategoryAfterAll = function(institution, category) {
      var index = findIndexOfAllForGivenCategory(category);
      $scope.institutions[category.name].splice(index+1, 0, institution);
    };
    var fillBadgesAndInstitutionsOfCategories = function(badgesInstitutions) {
      angular.forEach($scope.categories, function(category, index) {
        category.badge = badgesInstitutions[index].badge;
        $scope.institutions[category.name] = badgesInstitutions[index].who;
      });
    };
    var fillNewestEventsIfPresent = function(data) {
      if (data.hasOwnProperty('newest')) {
        $scope.newest = data.newest;
      }
    };
    var fillComingSoonEventsIfPresent = function(data) {
      if (data.hasOwnProperty('soon')) {
        $scope.soon = data.soon;
      }
    };
    
    $scope.openEventsCalendar = function($event, dayIndex) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.weekMenu.calendar.dayIndex = dayIndex;
      $scope.weekMenu.calendar.opened = true;
    };
    $scope.$watch('weekMenu.calendar.value', function(newVal, oldVal) {
      if (newVal !== oldVal && newVal !== undefined && newVal !== null &&
          $scope.weekMenu.calendar.dayIndex !== undefined) {
        if (oldVal !== undefined && oldVal !== null) {
          if (newVal.getTime() !== oldVal.getTime()) {
            $scope.changeEventsDay($scope.weekMenu.calendar.dayIndex);
          }
        } else {
          $scope.changeEventsDay($scope.weekMenu.calendar.dayIndex);
        }
      }
    });
    
    var cacheUserSettings = function() {
      var cache = getCache();
      cache.put(CACHE_INST_KEY, $scope.institutions.active);
      cache.put(CACHE_ORDER_KEY, $scope.orderEventsBy);
      cache.put(CACHE_CATEGORY_KEY, $scope.categories.active.index);
      cache.put(CACHE_DAY_KEY, $scope.weekMenu.active.index);
      cache.put(CACHE_CALENDAR_KEY, $scope.weekMenu.calendar.value);
      return cache;
    };
    $scope.displayEvent = function(eventId, scrollToWhenBack) {
      var cache = cacheUserSettings();
      if (scrollToWhenBack !== undefined && scrollToWhenBack) {
        cache.put(CACHE_EVENT_KEY, eventId);
      }
      $location.url('event/' + eventId);
    };
    
    var scrollTo = function(hash) {
      $timeout(function() {
        var old = $location.hash();
        $location.hash(hash);
        $anchorScroll();
        $location.hash(old);  //reset to old to keep any additional routing logic from kicking in, otherwise it renders twice
      }, 150);  // wait a little to get events rendered, otherwise it doesn't work
    };
    var scrollToEventIfNeeded = function() {
      var cache = getCache();
      var eventId = cache.get(CACHE_EVENT_KEY);
      if (eventId !== undefined) {
        cache.remove(CACHE_EVENT_KEY);
        scrollTo('event' + eventId);
      }
    };
  }]);
