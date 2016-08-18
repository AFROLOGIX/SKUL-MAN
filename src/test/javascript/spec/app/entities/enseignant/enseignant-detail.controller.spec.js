'use strict';

describe('Controller Tests', function() {

    describe('Enseignant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEnseignant, MockPersonnel, MockAbsenceEnseignant, MockVacation, MockFichier, MockProjetPedagogique, MockDeliberation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEnseignant = jasmine.createSpy('MockEnseignant');
            MockPersonnel = jasmine.createSpy('MockPersonnel');
            MockAbsenceEnseignant = jasmine.createSpy('MockAbsenceEnseignant');
            MockVacation = jasmine.createSpy('MockVacation');
            MockFichier = jasmine.createSpy('MockFichier');
            MockProjetPedagogique = jasmine.createSpy('MockProjetPedagogique');
            MockDeliberation = jasmine.createSpy('MockDeliberation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Enseignant': MockEnseignant,
                'Personnel': MockPersonnel,
                'AbsenceEnseignant': MockAbsenceEnseignant,
                'Vacation': MockVacation,
                'Fichier': MockFichier,
                'ProjetPedagogique': MockProjetPedagogique,
                'Deliberation': MockDeliberation
            };
            createController = function() {
                $injector.get('$controller')("EnseignantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:enseignantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
