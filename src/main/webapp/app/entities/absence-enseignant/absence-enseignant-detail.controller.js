(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEnseignantDetailController', AbsenceEnseignantDetailController);

    AbsenceEnseignantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AbsenceEnseignant', 'Jour', 'Enseignant'];

    function AbsenceEnseignantDetailController($scope, $rootScope, $stateParams, previousState, entity, AbsenceEnseignant, Jour, Enseignant) {
        var vm = this;

        vm.absenceEnseignant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:absenceEnseignantUpdate', function(event, result) {
            vm.absenceEnseignant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
