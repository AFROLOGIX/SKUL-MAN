'use strict';

describe('Controller Tests', function() {

    describe('EnseignantTitulaire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEnseignantTitulaire, MockClasse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEnseignantTitulaire = jasmine.createSpy('MockEnseignantTitulaire');
            MockClasse = jasmine.createSpy('MockClasse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EnseignantTitulaire': MockEnseignantTitulaire,
                'Classe': MockClasse
            };
            createController = function() {
                $injector.get('$controller')("EnseignantTitulaireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:enseignantTitulaireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
