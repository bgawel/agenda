'use strict';

describe('Controller: EventCtrl', function () {

  // load the controller's module
  beforeEach(module('frontendApp'));

  var EventCtrl, scope, $httpBackend;
  
  var EVENT_RESPONSE = {id: 555};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $routeParams, _$httpBackend_) {
    scope = $rootScope.$new();
    $routeParams.eventId = 666;
    $httpBackend = _$httpBackend_;
    $httpBackend.expectGET('events/id/' + $routeParams.eventId + '.json').respond(EVENT_RESPONSE);
    EventCtrl = $controller('EventCtrl', {$scope: scope});
  }));

  it('should create "event" model based on data fetched from xhr', function () {
    $httpBackend.flush();
    
    expect(scope.event).toEqual(EVENT_RESPONSE);
  });
});
