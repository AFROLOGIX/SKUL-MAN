'use strict';

describe('Controller Tests', function() {

    describe('ClasseEleve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClasseEleve, MockClasse, MockEleve;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClasseEleve = jasmine.createSpy('MockClasseEleve');
            MockClasse = jasmine.createSpy('MockClasse');
            MockEleve = jasmine.createSpy('MockEleve');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ClasseEleve': MockClasseEleve,
                'Classe': MockClasse,
                'Eleve': MockEleve
            };
            createController = function() {
                $injector.get('$controller')("ClasseEleveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:classeEleveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
