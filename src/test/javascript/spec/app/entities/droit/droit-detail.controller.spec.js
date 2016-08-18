'use strict';

describe('Controller Tests', function() {

    describe('Droit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDroit, MockFonctionnalite, MockGroupeUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDroit = jasmine.createSpy('MockDroit');
            MockFonctionnalite = jasmine.createSpy('MockFonctionnalite');
            MockGroupeUtilisateur = jasmine.createSpy('MockGroupeUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Droit': MockDroit,
                'Fonctionnalite': MockFonctionnalite,
                'GroupeUtilisateur': MockGroupeUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("DroitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:droitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
