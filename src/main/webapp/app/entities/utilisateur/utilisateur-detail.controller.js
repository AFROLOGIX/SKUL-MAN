(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('UtilisateurDetailController', UtilisateurDetailController);

    UtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Utilisateur', 'GroupeUtilisateur'];

    function UtilisateurDetailController($scope, $rootScope, $stateParams, previousState, entity, Utilisateur, GroupeUtilisateur) {
        var vm = this;

        vm.utilisateur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:utilisateurUpdate', function(event, result) {
            vm.utilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
