(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FichierDetailController', FichierDetailController);

    FichierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fichier', 'AgentAdministratif', 'Eleve', 'Enseignant'];

    function FichierDetailController($scope, $rootScope, $stateParams, previousState, entity, Fichier, AgentAdministratif, Eleve, Enseignant) {
        var vm = this;

        vm.fichier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:fichierUpdate', function(event, result) {
            vm.fichier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
