'use strict';

describe('Controller Tests', function() {

    describe('Deliberation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDeliberation, MockEleve, MockAgentAdministratif, MockEnseignant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDeliberation = jasmine.createSpy('MockDeliberation');
            MockEleve = jasmine.createSpy('MockEleve');
            MockAgentAdministratif = jasmine.createSpy('MockAgentAdministratif');
            MockEnseignant = jasmine.createSpy('MockEnseignant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Deliberation': MockDeliberation,
                'Eleve': MockEleve,
                'AgentAdministratif': MockAgentAdministratif,
                'Enseignant': MockEnseignant
            };
            createController = function() {
                $injector.get('$controller')("DeliberationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:deliberationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
