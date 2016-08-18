'use strict';

describe('Controller Tests', function() {

    describe('Chambre Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChambre, MockBatiment, MockTypeChambre;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChambre = jasmine.createSpy('MockChambre');
            MockBatiment = jasmine.createSpy('MockBatiment');
            MockTypeChambre = jasmine.createSpy('MockTypeChambre');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Chambre': MockChambre,
                'Batiment': MockBatiment,
                'TypeChambre': MockTypeChambre
            };
            createController = function() {
                $injector.get('$controller')("ChambreDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:chambreUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
