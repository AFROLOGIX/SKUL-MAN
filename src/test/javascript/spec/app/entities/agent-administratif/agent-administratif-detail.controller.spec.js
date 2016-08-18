'use strict';

describe('Controller Tests', function() {

    describe('AgentAdministratif Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAgentAdministratif, MockSalaire, MockFichier, MockDeliberation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAgentAdministratif = jasmine.createSpy('MockAgentAdministratif');
            MockSalaire = jasmine.createSpy('MockSalaire');
            MockFichier = jasmine.createSpy('MockFichier');
            MockDeliberation = jasmine.createSpy('MockDeliberation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AgentAdministratif': MockAgentAdministratif,
                'Salaire': MockSalaire,
                'Fichier': MockFichier,
                'Deliberation': MockDeliberation
            };
            createController = function() {
                $injector.get('$controller')("AgentAdministratifDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:agentAdministratifUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
