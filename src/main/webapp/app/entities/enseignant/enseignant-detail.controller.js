(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantDetailController', EnseignantDetailController);

    EnseignantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Enseignant', 'Personnel', 'AbsenceEnseignant', 'Vacation', 'Fichier', 'ProjetPedagogique', 'Deliberation'];

    function EnseignantDetailController($scope, $rootScope, $stateParams, previousState, entity, Enseignant, Personnel, AbsenceEnseignant, Vacation, Fichier, ProjetPedagogique, Deliberation) {
        var vm = this;

        vm.enseignant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:enseignantUpdate', function(event, result) {
            vm.enseignant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
