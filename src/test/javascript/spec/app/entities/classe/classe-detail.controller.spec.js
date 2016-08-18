'use strict';

describe('Controller Tests', function() {

    describe('Classe Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClasse, MockSerie, MockNiveau;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClasse = jasmine.createSpy('MockClasse');
            MockSerie = jasmine.createSpy('MockSerie');
            MockNiveau = jasmine.createSpy('MockNiveau');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Classe': MockClasse,
                'Serie': MockSerie,
                'Niveau': MockNiveau
            };
            createController = function() {
                $injector.get('$controller')("ClasseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:classeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
