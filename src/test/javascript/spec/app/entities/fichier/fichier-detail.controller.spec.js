'use strict';

describe('Controller Tests', function() {

    describe('Fichier Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFichier, MockAgentAdministratif, MockEleve, MockEnseignant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFichier = jasmine.createSpy('MockFichier');
            MockAgentAdministratif = jasmine.createSpy('MockAgentAdministratif');
            MockEleve = jasmine.createSpy('MockEleve');
            MockEnseignant = jasmine.createSpy('MockEnseignant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Fichier': MockFichier,
                'AgentAdministratif': MockAgentAdministratif,
                'Eleve': MockEleve,
                'Enseignant': MockEnseignant
            };
            createController = function() {
                $injector.get('$controller')("FichierDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:fichierUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
