'use strict';

describe('Controller Tests', function() {

    describe('TrancheHoraire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrancheHoraire, MockTypeTrancheHoraire;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrancheHoraire = jasmine.createSpy('MockTrancheHoraire');
            MockTypeTrancheHoraire = jasmine.createSpy('MockTypeTrancheHoraire');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TrancheHoraire': MockTrancheHoraire,
                'TypeTrancheHoraire': MockTypeTrancheHoraire
            };
            createController = function() {
                $injector.get('$controller')("TrancheHoraireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:trancheHoraireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
