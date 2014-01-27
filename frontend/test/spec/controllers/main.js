'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('frontendApp'));
  // load the BootstrapUI module
  beforeEach(module('ui.bootstrap'));

  var MainCtrl, scope, $httpBackend;
  
  var DAY_MENU_RESPONSE = {
      entries: [{name: 'all', abbr:'', id:'all'}, {name: 'today', abbr:'MON', id:'03-02-2014'}],
      activeIndex: 1
    };
  var CATEGORY_FILTER_RESPONSE = {
      entries: [{name: 'all', id:'all'}, {name: 'Theater', id:1}],
      activeIndex: 0
    };

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_) {
    $httpBackend = _$httpBackend_;
    scope = $rootScope.$new();
    $httpBackend.expectGET('metadata/week.json').respond(DAY_MENU_RESPONSE);
    $httpBackend.expectGET('metadata/categories.json').respond(CATEGORY_FILTER_RESPONSE);
    MainCtrl = $controller('MainCtrl', {$scope: scope});
  }));

  it('should create "week menu" model with 2 entries fetched from xhr', function () {
    $httpBackend.flush();
     
    expect(scope.weekMenu.length).toEqual(2);
    expect(JSON.stringify(scope.weekMenu[0])).toEqual(JSON.stringify(DAY_MENU_RESPONSE.entries[0]));
    expect(scope.weekMenu.activeIndex).toEqual(1);
    expect(scope.weekMenu[1].ngClass).toEqual('active');
  });
  
  it('should change events day', function () {
    $httpBackend.flush();
    scope.changeEventsDay(0);
    
    expect(scope.weekMenu.activeIndex).toEqual(0);
    expect(scope.weekMenu[1].ngClass).toEqual('');
    expect(scope.weekMenu[0].ngClass).toEqual('active');
  });
  
  it('should create "category filter" model with 2 entries fetched from xhr', function () {
    $httpBackend.flush();
     
    expect(scope.categories.length).toEqual(2);
    expect(JSON.stringify(scope.categories[1])).toEqual(JSON.stringify(CATEGORY_FILTER_RESPONSE.entries[1]));
    expect(scope.categories.activeIndex).toEqual(0);
    expect(scope.categories[0].ngClass).toEqual('active');
  });
  
  it('should change events day', function () {
    $httpBackend.flush();
    scope.changeCategory(1);
    
    expect(scope.categories.activeIndex).toEqual(1);
    expect(scope.categories[0].ngClass).toEqual('');
    expect(scope.categories[1].ngClass).toEqual('active');
  });
});
