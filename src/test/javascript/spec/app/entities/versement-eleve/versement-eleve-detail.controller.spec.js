'use strict';

describe('Controller Tests', function() {

    describe('VersementEleve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVersementEleve, MockAgentAdministratif, MockEleve, MockOperation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVersementEleve = jasmine.createSpy('MockVersementEleve');
            MockAgentAdministratif = jasmine.createSpy('MockAgentAdministratif');
            MockEleve = jasmine.createSpy('MockEleve');
            MockOperation = jasmine.createSpy('MockOperation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VersementEleve': MockVersementEleve,
                'AgentAdministratif': MockAgentAdministratif,
                'Eleve': MockEleve,
                'Operation': MockOperation
            };
            createController = function() {
                $injector.get('$controller')("VersementEleveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:versementEleveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
