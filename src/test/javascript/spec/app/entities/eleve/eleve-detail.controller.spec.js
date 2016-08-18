'use strict';

describe('Controller Tests', function() {

    describe('Eleve Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEleve, MockChambreEleve, MockCompte, MockReligion, MockAbsenceEleve, MockBourse, MockMoratoire, MockFichier;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEleve = jasmine.createSpy('MockEleve');
            MockChambreEleve = jasmine.createSpy('MockChambreEleve');
            MockCompte = jasmine.createSpy('MockCompte');
            MockReligion = jasmine.createSpy('MockReligion');
            MockAbsenceEleve = jasmine.createSpy('MockAbsenceEleve');
            MockBourse = jasmine.createSpy('MockBourse');
            MockMoratoire = jasmine.createSpy('MockMoratoire');
            MockFichier = jasmine.createSpy('MockFichier');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Eleve': MockEleve,
                'ChambreEleve': MockChambreEleve,
                'Compte': MockCompte,
                'Religion': MockReligion,
                'AbsenceEleve': MockAbsenceEleve,
                'Bourse': MockBourse,
                'Moratoire': MockMoratoire,
                'Fichier': MockFichier
            };
            createController = function() {
                $injector.get('$controller')("EleveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:eleveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
