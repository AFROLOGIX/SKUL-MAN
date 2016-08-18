'use strict';

describe('Controller Tests', function() {

    describe('PaiementPersonnel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPaiementPersonnel, MockAgentAdministratif, MockOperation, MockPersonnel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPaiementPersonnel = jasmine.createSpy('MockPaiementPersonnel');
            MockAgentAdministratif = jasmine.createSpy('MockAgentAdministratif');
            MockOperation = jasmine.createSpy('MockOperation');
            MockPersonnel = jasmine.createSpy('MockPersonnel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PaiementPersonnel': MockPaiementPersonnel,
                'AgentAdministratif': MockAgentAdministratif,
                'Operation': MockOperation,
                'Personnel': MockPersonnel
            };
            createController = function() {
                $injector.get('$controller')("PaiementPersonnelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:paiementPersonnelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
