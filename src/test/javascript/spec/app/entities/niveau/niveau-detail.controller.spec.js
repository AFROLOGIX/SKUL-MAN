'use strict';

describe('Controller Tests', function() {

    describe('Niveau Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNiveau, MockClasse, MockCycle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNiveau = jasmine.createSpy('MockNiveau');
            MockClasse = jasmine.createSpy('MockClasse');
            MockCycle = jasmine.createSpy('MockCycle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Niveau': MockNiveau,
                'Classe': MockClasse,
                'Cycle': MockCycle
            };
            createController = function() {
                $injector.get('$controller')("NiveauDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:niveauUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
