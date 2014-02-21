'use strict';

describe('Controller: EventCtrl', function () {

  // load the controller's module
  beforeEach(module('frontendApp'));

  var EventCtrl, scope, $httpBackend, $routeParams, $controller;
  
  var EVENT_RESPONSE = {id: 555};

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, $rootScope, _$routeParams_, _$httpBackend_) {
    scope = $rootScope.$new();
    $routeParams = _$routeParams_;
    $routeParams.eventId = 666;
    $httpBackend = _$httpBackend_;
    $controller = _$controller_;
  }));

  it('should create "event" model based on data fetched from xhr', function () {
    $httpBackend.expectGET('b/evntPres/byEvent/' + $routeParams.eventId + '.json').respond(EVENT_RESPONSE);
    $controller('EventCtrl', {$scope: scope});
    $httpBackend.flush();
    
    expect(scope.event).toEqual(EVENT_RESPONSE);
    expect(scope.eventGone).toBeFalsy();
  });
  
  it('should handle error response from server', function () {
    $httpBackend.expectGET('b/evntPres/byEvent/' + $routeParams.eventId + '.json').respond(410);
    $controller('EventCtrl', {$scope: scope});
    $httpBackend.flush();
    
    expect(scope.event).toBeUndefined();
    expect(scope.eventGone).toBeTruthy();
  });
});
