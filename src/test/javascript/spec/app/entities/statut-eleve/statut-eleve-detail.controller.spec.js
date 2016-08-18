'use strict';

describe('Controller Tests', function() {

    describe('StatutEleve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStatutEleve, MockEleve, MockStatut;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStatutEleve = jasmine.createSpy('MockStatutEleve');
            MockEleve = jasmine.createSpy('MockEleve');
            MockStatut = jasmine.createSpy('MockStatut');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StatutEleve': MockStatutEleve,
                'Eleve': MockEleve,
                'Statut': MockStatut
            };
            createController = function() {
                $injector.get('$controller')("StatutEleveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:statutEleveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
