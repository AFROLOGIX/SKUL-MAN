'use strict';

describe('Controller Tests', function() {

    describe('Epreuve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEpreuve, MockTypeEpreuve, MockCours;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEpreuve = jasmine.createSpy('MockEpreuve');
            MockTypeEpreuve = jasmine.createSpy('MockTypeEpreuve');
            MockCours = jasmine.createSpy('MockCours');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Epreuve': MockEpreuve,
                'TypeEpreuve': MockTypeEpreuve,
                'Cours': MockCours
            };
            createController = function() {
                $injector.get('$controller')("EpreuveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:epreuveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
