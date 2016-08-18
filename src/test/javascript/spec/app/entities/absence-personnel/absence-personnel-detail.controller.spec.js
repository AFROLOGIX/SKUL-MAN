'use strict';

describe('Controller Tests', function() {

    describe('AbsencePersonnel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAbsencePersonnel, MockJour, MockPersonnel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAbsencePersonnel = jasmine.createSpy('MockAbsencePersonnel');
            MockJour = jasmine.createSpy('MockJour');
            MockPersonnel = jasmine.createSpy('MockPersonnel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AbsencePersonnel': MockAbsencePersonnel,
                'Jour': MockJour,
                'Personnel': MockPersonnel
            };
            createController = function() {
                $injector.get('$controller')("AbsencePersonnelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:absencePersonnelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
