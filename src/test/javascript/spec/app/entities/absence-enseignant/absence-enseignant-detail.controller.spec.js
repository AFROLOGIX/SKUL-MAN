'use strict';

describe('Controller Tests', function() {

    describe('AbsenceEnseignant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAbsenceEnseignant, MockJour, MockEnseignant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAbsenceEnseignant = jasmine.createSpy('MockAbsenceEnseignant');
            MockJour = jasmine.createSpy('MockJour');
            MockEnseignant = jasmine.createSpy('MockEnseignant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AbsenceEnseignant': MockAbsenceEnseignant,
                'Jour': MockJour,
                'Enseignant': MockEnseignant
            };
            createController = function() {
                $injector.get('$controller')("AbsenceEnseignantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:absenceEnseignantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
