(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantTitulaireDetailController', EnseignantTitulaireDetailController);

    EnseignantTitulaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EnseignantTitulaire', 'Classe'];

    function EnseignantTitulaireDetailController($scope, $rootScope, $stateParams, previousState, entity, EnseignantTitulaire, Classe) {
        var vm = this;

        vm.enseignantTitulaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:enseignantTitulaireUpdate', function(event, result) {
            vm.enseignantTitulaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
