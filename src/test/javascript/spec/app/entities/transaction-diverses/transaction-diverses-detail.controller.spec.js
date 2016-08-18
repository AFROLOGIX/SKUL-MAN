'use strict';

describe('Controller Tests', function() {

    describe('TransactionDiverses Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTransactionDiverses, MockAgentAdministratif, MockOperation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTransactionDiverses = jasmine.createSpy('MockTransactionDiverses');
            MockAgentAdministratif = jasmine.createSpy('MockAgentAdministratif');
            MockOperation = jasmine.createSpy('MockOperation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TransactionDiverses': MockTransactionDiverses,
                'AgentAdministratif': MockAgentAdministratif,
                'Operation': MockOperation
            };
            createController = function() {
                $injector.get('$controller')("TransactionDiversesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:transactionDiversesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
