'use strict';

describe('Controller Tests', function() {

    describe('Personnel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPersonnel, MockUtilisateur, MockTypePersonnel, MockAbsencePersonnel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPersonnel = jasmine.createSpy('MockPersonnel');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            MockTypePersonnel = jasmine.createSpy('MockTypePersonnel');
            MockAbsencePersonnel = jasmine.createSpy('MockAbsencePersonnel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Personnel': MockPersonnel,
                'Utilisateur': MockUtilisateur,
                'TypePersonnel': MockTypePersonnel,
                'AbsencePersonnel': MockAbsencePersonnel
            };
            createController = function() {
                $injector.get('$controller')("PersonnelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'skulmanApp:personnelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
