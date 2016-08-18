'use strict';

describe('Controller Tests', function() {

    describe('ProjetPedagogique Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjetPedagogique, MockClasse, MockEnseignant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjetPedagogique = jasmine.createSpy('MockProjetPedagogique');
            MockClasse = jasmine.createSpy('MockClasse');
            MockEnseignant = jasmine.createSpy('MockEnseignant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjetPedagogique': MockProjetPedagogique,
                'Classe': MockClasse,
                'Enseignant': MockEnseignant
            };
            createController = function() {
                $injector.get('$controller')("ProjetPedagogiqueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:projetPedagogiqueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
