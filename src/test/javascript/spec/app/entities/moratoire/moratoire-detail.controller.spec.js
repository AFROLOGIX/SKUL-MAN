'use strict';

describe('Controller Tests', function() {

    describe('Moratoire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMoratoire, MockTypeMoratoire, MockEleve;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMoratoire = jasmine.createSpy('MockMoratoire');
            MockTypeMoratoire = jasmine.createSpy('MockTypeMoratoire');
            MockEleve = jasmine.createSpy('MockEleve');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Moratoire': MockMoratoire,
                'TypeMoratoire': MockTypeMoratoire,
                'Eleve': MockEleve
            };
            createController = function() {
                $injector.get('$controller')("MoratoireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:moratoireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
