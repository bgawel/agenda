'use strict';

describe('Controller: PanelCtrl', function () {

  // load the controller's module
  beforeEach(module('frontendApp'));

  var PanelCtrl, scope, $httpBackend, $routeParams, $controller;
  
  var INST_RESPONSE = {
    id: 1,
    name: 'inst-1',
    pwd: '1234'
  };
  var CATEGORIES_RESPONSE = [
     {
         name: 'Teatr', 
         id: 1
     }, 
     {
         name: 'Taniec', 
         id: 2
     }, 
     {
         name: 'Performance', 
         id: 3
     }
  ];
  var SUBMITTED_RESPONSE = [
     {
       id: 1,
       title: 'Mayday',
       locdate: 'Scena na Świebodzkim, 21-01-2013, 19:00 (3)',
       mod: '2014-02-01T18:15:00'
     }                         
  ];

  // Initialize the controller and a mock scope
  beforeEach(inject(function (_$controller_, $rootScope, _$httpBackend_, _$routeParams_) {
    $controller = _$controller_;
    $httpBackend = _$httpBackend_;
    $routeParams = _$routeParams_;
    scope = $rootScope.$new();
    $routeParams.instId = 666;
    $httpBackend.whenGET('b/config/now.json').respond({dateTime: '2014-12-24T19:00'});
    $httpBackend.expectGET('b/inst/666.json').respond(INST_RESPONSE);
  }));

  it('should initialize model with view of institution details', function () {
    $controller('PanelCtrl', {$scope: scope});
    $httpBackend.flush();
    
    expect(scope.option).toBe(1);
    expect(scope.partial).toBe('panelInst.html');
    expect(scope.instMsgPanel.show).toBe(false);
    expect(scope.inst.name).toBe(INST_RESPONSE.name);
    expect(scope.inst.password).toBe(scope.inst.repwd);
  });
  
  it('should put inst data and handle successful response having clicked save', function () {
    $controller('PanelCtrl', {$scope: scope});
    $httpBackend.flush();
    scope.form.inst = {$invalid: false};
    
    $httpBackend.expectPUT('b/inst/1.json').respond(INST_RESPONSE);
    scope.saveInst();
    $httpBackend.flush();
    
    expect(scope.instMsgPanel.show).toBe(true);
    expect(scope.instMsgPanel.type).toBe('success');
    expect(scope.instMsgPanel.messages).toEqual(['Dane zostały zaktualizowane']);
  });
  
  it('should put inst data and handle unsuccessful response having clicked save', function () {
    $controller('PanelCtrl', {$scope: scope});
    $httpBackend.flush();
    scope.form.inst = {$invalid: false};
    
    $httpBackend.expectPUT('b/inst/1.json').respond(422, {global: 'Not saved'});
    scope.saveInst();
    $httpBackend.flush();
    
    expect(scope.instMsgPanel.show).toBe(true);
    expect(scope.instMsgPanel.type).toBe('danger');
    expect(scope.instMsgPanel.messages).toEqual(['Not saved']);
  });
  
  it('should post inst data and handle unexpected error having clicked save', function () {
    $controller('PanelCtrl', {$scope: scope});
    $httpBackend.flush();
    scope.form.inst = {$invalid: false};
    
    $httpBackend.expectPUT('b/inst/1.json').respond(404);
    scope.saveInst();
    $httpBackend.flush();
    
    expect(scope.instMsgPanel.show).toBe(true);
    expect(scope.instMsgPanel.type).toBe('danger');
    expect(scope.instMsgPanel.messages).toEqual(['Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl']);
  });
  
  it('should not post inst data if form is invalid having clicked save', function () {
    $controller('PanelCtrl', {$scope: scope});
    $httpBackend.flush();
    scope.form.inst = {$invalid: true};
    
    scope.saveInst();
    
    $httpBackend.verifyNoOutstandingRequest();
  });
  
  it('should initialize model with view of a new event', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    
    expect(scope.option).toBe(2);
    expect(scope.partial).toBe('panelEvent.html');
    expect(scope.cancelClicked).toBeUndefined();
    expect(scope.event.id).toBeDefined()
    expect(scope.evtMsgPanel.show).toBe(false);
    expect(scope.categories.length).toBe(3);
    expect(scope.event.pdtps[0].startTime).toBeDefined();
  });
  
  it('should add a new place / date / price section initialized with previous values', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.event.pdtps[0].place = 'Sky Tower';
    scope.event.pdtps[0].startTime = new Date();
    scope.event.pdtps[0].date = new Date();
    scope.event.pdtps[0].price = '15 euro'
    
    scope.addPdtp(0);
    
    expect(scope.event.pdtps.length).toBe(2);
    expect(scope.event.pdtps[1].place).toBe(scope.event.pdtps[0].place);
    expect(scope.event.pdtps[1].startTime).toBe(scope.event.pdtps[0].startTime);
    expect(scope.event.pdtps[1].date).toBeUndefined();
    expect(scope.event.pdtps[1].price).toBe(scope.event.pdtps[0].price);
  });
  
  it('should remove a place / date / price section', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.event.pdtps[0].date = new Date();
    scope.addPdtp(0);
    var secondSection = scope.event.pdtps[1];
    
    scope.removePdtp(0);
    
    expect(scope.event.pdtps.length).toBe(1);
    expect(scope.event.pdtps[0]).toBe(secondSection);
  });
  
  it('should allow to remove a place / date / price section', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.addPdtp(0);
    
    expect(scope.isRemovePdtpShown(scope.event.pdtps[0])).toBeTruthy();
  });
  
  it('should not allow to remove a place / date / price section if it is readonly', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.event.pdtps[0].readonly = true;
    scope.addPdtp(0);
    
    expect(scope.isRemovePdtpShown(scope.event.pdtps[0])).toBeFalsy();
  });
  
  it('should not allow to remove a place / date / price section if there is only one', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    
    expect(scope.isRemovePdtpShown(scope.event.pdtps[0])).toBeFalsy();
  });
  
  it('should post event data and handle successful response having clicked save', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: false};
    
    $httpBackend.expectPOST('b/event.json').respond({id:2});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    scope.saveEvent();
    $httpBackend.flush();
    
    expect(scope.evtMsgPanel.show).toBe(true);
    expect(scope.evtMsgPanel.type).toBe('success');
    expect(scope.evtMsgPanel.messages).toEqual(['Nowe wydarzenie zostało dodane']);
  });
  
  it('should post event data and handle unsuccessful response having clicked save', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: false};
    
    $httpBackend.expectPOST('b/event.json').respond(500);
    scope.saveEvent();
    $httpBackend.flush();
    
    expect(scope.evtMsgPanel.show).toBe(true);
    expect(scope.evtMsgPanel.type).toBe('danger');
    expect(scope.evtMsgPanel.messages).toEqual(['Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl']);
  });
  
  it('should post event data and handle unexpected error having clicked save', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: false};
    
    $httpBackend.expectPOST('b/event.json').respond(404);
    scope.saveEvent();
    $httpBackend.flush();
    
    expect(scope.evtMsgPanel.show).toBe(true);
    expect(scope.evtMsgPanel.type).toBe('danger');
    expect(scope.evtMsgPanel.messages[0]).toBeDefined();
  });
  
  it('should not post event data if form is invalid', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: true};
    
    scope.saveEvent();
    
    $httpBackend.verifyNoOutstandingRequest();
  });
  
  var spyUploader = function() {
    scope.uploader.getNotUploadedItems = function() {
      return [{}];
    };
    var uploadCalled = false;
    scope.uploader.uploadAll = function() {
      uploadCalled = true;
    };
    return {
      verify : function() {
        expect(uploadCalled).toBeTruthy();
      }
    };
  };
  
  it('should upload picture and post event data', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: false};
    var spiedUploader = spyUploader();

    $httpBackend.expectPOST('b/event.json').respond({id:2});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    scope.saveEvent();
    scope.uploader.trigger('completeall');
    $httpBackend.flush();
    
    spiedUploader.verify();
  });
  
  it('should not post event data if could not upload picture, display received response', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: false};
    var spiedUploader = spyUploader();

    scope.saveEvent();
    scope.uploader.trigger('error', {}, {}, {});
    
    spiedUploader.verify();
    $httpBackend.verifyNoOutstandingRequest();
    expect(scope.evtMsgPanel.show).toBe(true);
    expect(scope.evtMsgPanel.type).toBe('danger');
    expect(scope.evtMsgPanel.messages[0]).toBe('Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl');
  });
  
  it('should not post event data if could not upload picture, display default response', function () {
    $routeParams.o = '2';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.flush();
    scope.form.event = {$invalid: false};
    var spiedUploader = spyUploader();

    scope.saveEvent();
    scope.uploader.trigger('error', {}, {}, {});
    
    spiedUploader.verify();
    $httpBackend.verifyNoOutstandingRequest();
    expect(scope.evtMsgPanel.show).toBe(true);
    expect(scope.evtMsgPanel.type).toBe('danger');
    expect(scope.evtMsgPanel.messages[0]).toBe('Ups. Niespodziewany błąd, spróbuj ponownie albo poinformuj nas o błędzie - info@agenda.pl');
  });
  
  it('should initialize model with view of submitted events', function () {
    $routeParams.o = '3';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/evntProj/submitted/666.json').respond(SUBMITTED_RESPONSE);
    $httpBackend.flush();
    
    expect(scope.option).toBe(3);
    expect(scope.partial).toBe('panelSubmittedEvents.html');
  });
  
  it('should load existing event', function () {
    $routeParams.o = '3';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/evntProj/submitted/666.json').respond(SUBMITTED_RESPONSE);
    $httpBackend.flush();
    
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.expectGET('b/event/1.json').respond({title:'Mayday'});
    scope.loadExistingEvent(1);
    $httpBackend.flush();
    
    expect(scope.partial).toBe('panelEvent.html');
    expect(scope.reloadSubmittedEvent).toBeDefined();
    expect(scope.cancelClicked).toBeDefined();
    expect(scope.option).toBe(4);
  });
  
  it('should cancel editing an existing event', function () {
    $routeParams.o = '3';
    $controller('PanelCtrl', {$scope: scope, $routeParams: $routeParams});
    $httpBackend.expectGET('b/evntProj/submitted/666.json').respond(SUBMITTED_RESPONSE);
    $httpBackend.flush();
    $httpBackend.expectGET('b/category/all.json').respond(CATEGORIES_RESPONSE);
    $httpBackend.expectGET('b/event/1.json').respond({title:'Mayday'});
    scope.loadExistingEvent(1);
    $httpBackend.flush();
    
    $httpBackend.expectGET('b/evntProj/submitted/666.json').respond(SUBMITTED_RESPONSE);
    scope.cancelClicked();
    $httpBackend.flush();
    
    expect(scope.partial).toBe('panelSubmittedEvents.html');
    expect(scope.option).toBe(3);
  });
});
