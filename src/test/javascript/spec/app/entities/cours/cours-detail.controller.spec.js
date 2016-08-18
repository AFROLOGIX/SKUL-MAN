'use strict';

describe('Controller Tests', function() {

    describe('Cours Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCours, MockMatiere, MockClasse, MockEnseignant, MockEpreuve, MockNote;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCours = jasmine.createSpy('MockCours');
            MockMatiere = jasmine.createSpy('MockMatiere');
            MockClasse = jasmine.createSpy('MockClasse');
            MockEnseignant = jasmine.createSpy('MockEnseignant');
            MockEpreuve = jasmine.createSpy('MockEpreuve');
            MockNote = jasmine.createSpy('MockNote');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Cours': MockCours,
                'Matiere': MockMatiere,
                'Classe': MockClasse,
                'Enseignant': MockEnseignant,
                'Epreuve': MockEpreuve,
                'Note': MockNote
            };
            createController = function() {
                $injector.get('$controller')("CoursDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:coursUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
