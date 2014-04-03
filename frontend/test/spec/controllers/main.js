'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('frontendApp'));

  var MainCtrl, scope, $httpBackend, $cookies, $location;
  
  var CATEGORY_FILTER_RESPONSE = {
      entries: [{name: 'all', id:'all'}, {name: 'Theater', id:'1'}, {name: 'For Children', id:'2'}],
      activeIndex: 0
    };
  var WEEK_MENU_RESPONSE = {
      entries: [{name: 'All', abbr:'', id:'all', type:'all'}, {name: 'today', abbr:'MON', id:'2014-02-03', type:'today'}, 
                {name: '', abbr:'', id:'cal', type:'cal'}],
      activeIndex: 1
    };
  var EVENTS_RESPONSE = {
      categories: [
        {
          id: 'all',
          badge: 1,
          who: [
            {
              id: 'all',
              name: 'All',
              badge: 1
            },
            {
              id: '1',
              name: 'Polish Theater',
              badge: 1
            },
            {
              id: '2',
              name: 'For Children',
              badge: 0
            }
          ]
        },
        {
          id: '1',
          badge: 1,
          who: [
            {
              id: 'all',
              name: 'All',
              badge: 1
            },
            {
              id: '1',
              name: 'Polish Theater',
              badge: 1
            }
          ]
        },
        {
          id: '2',
          badge: 0,
          who: [
            {
              id: 'all',
              name: 'All',
              badge: 0
            }
          ]
        }
      ],
      events: [
        {
          id: 1,
          title: 'Mayday',
          whoName: 'Polish Theater',
          catName: 'Theater'
        }
      ],
      newest: [
        {
          id: 5,
          text: 'text 5'
        }
      ],
      soon: [
         {
           id: 7,
           text: 'text 7'
         }
      ]
    };  

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, _$cookies_, _$location_, $templateCache) {
    $templateCache.put('views/main.html', '<i-do-not-know-why-this-is-needed/>');
    $templateCache.put('views/event.html', '<i-do-not-know-why-this-is-needed/>');
    $httpBackend = _$httpBackend_;
    $cookies = _$cookies_;
    $location = _$location_;
    scope = $rootScope.$new();
    $httpBackend.expectGET('b/menu/categories.json').respond(CATEGORY_FILTER_RESPONSE);
    $httpBackend.expectGET('b/menu/week.json').respond(WEEK_MENU_RESPONSE);
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-03.json?category=all').respond(EVENTS_RESPONSE);
    MainCtrl = $controller('MainCtrl', {$scope: scope});
  }));

  it('should toggle right panel', function () {
    scope.toggleRightPanel();
    expect(scope.rightPanel).toBe('activeRight');
    
    scope.toggleRightPanel();
    expect(scope.rightPanel).toBe('');
  });
  
  it('should toggle left panel', function () {
    scope.toggleLeftPanel();
    expect(scope.leftPanel).toBe('activeLeft');
    
    scope.toggleLeftPanel();
    expect(scope.leftPanel).toBe('');
  });
  
  it('should create "categories" model with entries fetched from xhr', function () {
    $httpBackend.flush();
     
    expect(scope.categories.length).toEqual(3);
    var activeCategory = scope.categories.active;
    expect(activeCategory).toBe(scope.categories[0]);
    expect(activeCategory.ngClass).toEqual('active');
    expect(activeCategory.index).toEqual(0);
    expect(activeCategory.id).toEqual(CATEGORY_FILTER_RESPONSE.entries[0].id);
    expect(activeCategory.name).toEqual(CATEGORY_FILTER_RESPONSE.entries[0].name);
    expect(activeCategory.badge).toEqual(EVENTS_RESPONSE.categories[0].badge);
    expect(scope.categories.filter).toBeUndefined();
  });
  
  it('should change active category to another category', function () {
    $httpBackend.flush();
    
    scope.changeCategory(1);
    
    var activeCategory = scope.categories.active;
    expect(activeCategory).toBe(scope.categories[1]);
    expect(scope.categories[0].ngClass).toBe('');
    expect(activeCategory.ngClass).toEqual('active');
    expect(activeCategory.index).toEqual(1);
    expect(activeCategory.id).toEqual(CATEGORY_FILTER_RESPONSE.entries[1].id);
    expect(activeCategory.name).toEqual(CATEGORY_FILTER_RESPONSE.entries[1].name);
    expect(activeCategory.badge).toEqual(EVENTS_RESPONSE.categories[1].badge);
    expect(scope.categories.filter).toBe(activeCategory);
  });
  
  it('should change institution to all having changed a category', function () {
    $httpBackend.flush();    
    scope.changeInstitution(1);
    expect(scope.institutions.active.id).not.toBe('all');
    
    scope.changeCategory(1);
    
    expect(scope.institutions.active.id).toBe('all');
  });
  
  it('should create "intitutions" model with entries fetched from xhr', function () {
    $httpBackend.flush();
     
    var categoryAll = CATEGORY_FILTER_RESPONSE.entries[0];
    var categoryTheater = CATEGORY_FILTER_RESPONSE.entries[1];
    expect(scope.institutions[categoryAll.id].length).toBe(3);
    expect(scope.institutions[categoryTheater.id].length).toBe(2);
    var activeInstitution = scope.institutions.active;
    expect(activeInstitution).toBe(scope.institutions[categoryAll.id][0]);
    expect(activeInstitution.ngClass).toEqual('active');
    expect(activeInstitution.id).toEqual(EVENTS_RESPONSE.categories[0].who[0].id);
    expect(activeInstitution.name).toEqual(EVENTS_RESPONSE.categories[0].who[0].name);
    expect(activeInstitution.badge).toEqual(EVENTS_RESPONSE.categories[0].who[0].badge);
    expect(scope.institutions.filter).toBeUndefined();
  });
  
  it('should change active institution to another institution', function () {
    $httpBackend.flush();
    
    scope.changeInstitution(1);
    
    var categoryAll = CATEGORY_FILTER_RESPONSE.entries[0];
    var activeInstitution = scope.institutions.active;
    expect(activeInstitution).toBe(scope.institutions[categoryAll.name][1]);
    expect(scope.institutions[categoryAll.name][0].ngClass).toBe('');
    expect(activeInstitution.ngClass).toEqual('active');
    expect(activeInstitution.id).toEqual(EVENTS_RESPONSE.categories[0].who[1].id);
    expect(activeInstitution.name).toEqual(EVENTS_RESPONSE.categories[0].who[1].name);
    expect(activeInstitution.badge).toEqual(EVENTS_RESPONSE.categories[0].who[1].badge);
    expect(scope.institutions.filter).toBe(activeInstitution);
  });
  
  it('should create "week menu" with entries fetched from xhr', function () {
    $httpBackend.flush();
     
    expect(scope.weekMenu.length).toEqual(3);
    var activeDay = scope.weekMenu.active;
    expect(activeDay).toBe(scope.weekMenu[1]);
    expect(activeDay.ngClass).toEqual('active');
    expect(activeDay.index).toEqual(1);
    expect(activeDay.id).toEqual(WEEK_MENU_RESPONSE.entries[1].id);
    expect(activeDay.name).toEqual(WEEK_MENU_RESPONSE.entries[1].name);
    expect(activeDay.abbr).toEqual(WEEK_MENU_RESPONSE.entries[1].abbr);
    expect(scope.categories.active.id).toBe('all');
    expect(scope.institutions.active.id).toBe('all');
  });
  
  it('should change events day to a day picked up from a calendar', function () {
    $httpBackend.flush();
    scope.weekMenu.calendar.dayIndex = 2;
    
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    scope.weekMenu.calendar.value = new Date(2014, 1, 16);
    scope.$apply();
    $httpBackend.flush(); 
    
    var activeDay = scope.weekMenu.active;
    expect(activeDay).toBe(scope.weekMenu[2]);
    expect(scope.weekMenu[1].ngClass).toEqual('');
    expect(activeDay.ngClass).toEqual('active');
    expect(activeDay.index).toEqual(2);
    expect(activeDay.id).toEqual('2014-02-16');
    expect(activeDay.name).toEqual(WEEK_MENU_RESPONSE.entries[2].name);
    expect(activeDay.abbr).toEqual(WEEK_MENU_RESPONSE.entries[2].abbr);
  });
  
  it('should pick up another day from a calendar', function () {
    $httpBackend.flush();
    scope.weekMenu.calendar.dayIndex = 2;
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    scope.weekMenu.calendar.value = new Date(2014, 1, 16);
    scope.$apply();
    $httpBackend.flush(); 
    
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-17.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    scope.weekMenu.calendar.value = new Date(2014, 1, 17);
    scope.$apply();
    $httpBackend.flush(); 
    
    expect(scope.weekMenu.active.id).toEqual('2014-02-17');
  });
  
  it('should not trigger request if date from a calendar has not changed', function () {
    $httpBackend.flush();
    scope.weekMenu.calendar.dayIndex = 2;
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    scope.weekMenu.calendar.value = new Date(2014, 1, 16);
    scope.$apply();
    $httpBackend.flush(); 
    
    scope.weekMenu.calendar.value = new Date(2014, 1, 16);
    scope.$apply();
    
    $httpBackend.verifyNoOutstandingRequest();
  });
  
  it('should set calendar value to null if changed to other option', function () {
    $httpBackend.flush();
    scope.weekMenu.calendar.dayIndex = 2;
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    scope.weekMenu.calendar.value = new Date(2014, 1, 16);
    scope.$apply();
    $httpBackend.flush(); 
    
    $httpBackend.whenGET('b/evntProj/byDate/all.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    scope.changeEventsDay(0);
    $httpBackend.flush();
    
    expect(scope.weekMenu.calendar.value).toBeNull();
    $httpBackend.verifyNoOutstandingRequest();
  });
  
  it('should create models for events with entries fetched from xhr', function () {
    $httpBackend.flush();
     
    expect(scope.events).toEqual(EVENTS_RESPONSE.events);
    expect(scope.newest).toEqual(EVENTS_RESPONSE.newest);
    expect(scope.soon).toEqual(EVENTS_RESPONSE.soon);
  });
  
  it('should change events day but active institution should stay', function () {
    $httpBackend.flush();
    scope.changeInstitution(1);
    expect(scope.weekMenu.active.id).not.toBe('all');
    expect(scope.categories.active.badge).toBe(1);
    expect(scope.institutions.active.name).toBe('Polish Theater');
    expect(scope.institutions.active.badge).toBe(1);

    $httpBackend.whenGET('b/evntProj/byDate/all.json?category=all&inst=1').respond(EVENTS_RESPONSE);
    scope.changeEventsDay(0);
    $httpBackend.flush();
    
    expect(scope.weekMenu.active.id).toBe('all');
    expect(scope.categories.active.badge).toBe(1);
    expect(scope.institutions.active.name).toBe('Polish Theater');
    expect(scope.institutions.active.badge).toBe(1);
  });
  
  it('should change events day but active institution should stay even if no events for the institution', function () {
    $httpBackend.flush();
    scope.changeInstitution(1);
    expect(scope.weekMenu.active.id).not.toBe('2014-02-16');
    expect(scope.categories.active.badge).toBe(1);
    expect(scope.institutions.active.name).toBe('Polish Theater');
    expect(scope.institutions.active.badge).toBe(1);
    
    scope.weekMenu.calendar.dayIndex = 2;
    var response = {
          categories: [
               {
                 id: 'all',
                 badge: 0,
                 who: [
                   {
                     id: 'all',
                     name: 'All',
                     badge: 0
                   }
                 ]
               },
               {
                 id: '1',
                 badge: 0,
                 who: [
                   {
                     id: 'all',
                     name: 'All',
                     badge: 0
                   }
                 ]
               },
               {
                 id: '2',
                 badge: 0,
                 who: [
                   {
                     id: 'all',
                     name: 'All',
                     badge: 0
                   }
                 ]
               }
             ],
        events: []
    };  
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=all&inst=1').respond(response);
    scope.weekMenu.calendar.value = new Date(2014, 1, 16);
    scope.$apply();
    $httpBackend.flush(); 
    
    expect(scope.weekMenu.active.id).toBe('2014-02-16');
    expect(scope.categories.active.badge).toBe(0);
    expect(scope.institutions.active.name).toBe('Polish Theater');
    expect(scope.institutions.active.badge).toBe(0);
    expect(scope.newest).toEqual(EVENTS_RESPONSE.newest);
    expect(scope.soon).toEqual(EVENTS_RESPONSE.soon);
  });
  
  it('should save user settings', function () {
    $httpBackend.flush();
    scope.weekMenu.calendar.dayIndex = 2;
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=all&inst=all').respond(EVENTS_RESPONSE);
    var date = new Date(2014, 1, 16);
    scope.weekMenu.calendar.value = date;
    scope.$apply();
    $httpBackend.flush();
     
    scope.displayEvent(1, true);
    
    expect($cookies.search).toEqual('{"s":"dateTime","c":"all","i":"all","cal":"2014-02-16"}');
    expect($location.path()).toEqual('/event/1');
  });
  
  it('should initialize model with saved settings', function () {
    $httpBackend.flush();
    scope.changeCategory(1);
    scope.changeInstitution(1);
    scope.weekMenu.calendar.dayIndex = 2;
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=1&inst=1').respond(EVENTS_RESPONSE);
    var date = new Date(2014, 1, 16);
    scope.weekMenu.calendar.value = date;
    scope.$apply();
    $httpBackend.flush();
    
    scope.saveFilters();
    $httpBackend.expectGET('b/menu/categories.json').respond(CATEGORY_FILTER_RESPONSE);
    $httpBackend.expectGET('b/menu/week.json').respond(WEEK_MENU_RESPONSE);
    $httpBackend.expectGET('b/evntProj/byDate/2014-02-16.json?category=1&inst=1').respond(EVENTS_RESPONSE);
    scope.init();
    $httpBackend.flush();
    
    expect($cookies.search).toBeUndefined();
    expect(scope.categories.active).toBe(scope.categories[1]);
    expect(scope.institutions.active).toBe(scope.institutions[scope.categories.active.id][1]);
    expect(scope.weekMenu.active).toBe(scope.weekMenu[2]);
    expect(scope.weekMenu.calendar.value).toEqual('2014-02-16');
  });
});
