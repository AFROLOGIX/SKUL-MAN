(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PersonnelDetailController', PersonnelDetailController);

    PersonnelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Personnel', 'Utilisateur', 'TypePersonnel', 'AbsencePersonnel'];

    function PersonnelDetailController($scope, $rootScope, $stateParams, previousState, entity, Personnel, Utilisateur, TypePersonnel, AbsencePersonnel) {
        var vm = this;

        vm.personnel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:personnelUpdate', function(event, result) {
            vm.personnel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
