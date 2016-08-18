'use strict';

describe('Controller Tests', function() {

    describe('MatiereClasse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMatiereClasse, MockClasse, MockMatiere;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMatiereClasse = jasmine.createSpy('MockMatiereClasse');
            MockClasse = jasmine.createSpy('MockClasse');
            MockMatiere = jasmine.createSpy('MockMatiere');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MatiereClasse': MockMatiereClasse,
                'Classe': MockClasse,
                'Matiere': MockMatiere
            };
            createController = function() {
                $injector.get('$controller')("MatiereClasseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:matiereClasseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
