'use strict';

describe('Controller Tests', function() {

    describe('AbsenceEleve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAbsenceEleve, MockJour, MockEleve;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAbsenceEleve = jasmine.createSpy('MockAbsenceEleve');
            MockJour = jasmine.createSpy('MockJour');
            MockEleve = jasmine.createSpy('MockEleve');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AbsenceEleve': MockAbsenceEleve,
                'Jour': MockJour,
                'Eleve': MockEleve
            };
            createController = function() {
                $injector.get('$controller')("AbsenceEleveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:absenceEleveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
