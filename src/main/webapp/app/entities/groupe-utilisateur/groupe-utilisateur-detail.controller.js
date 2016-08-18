(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('GroupeUtilisateurDetailController', GroupeUtilisateurDetailController);

    GroupeUtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GroupeUtilisateur'];

    function GroupeUtilisateurDetailController($scope, $rootScope, $stateParams, previousState, entity, GroupeUtilisateur) {
        var vm = this;

        vm.groupeUtilisateur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:groupeUtilisateurUpdate', function(event, result) {
            vm.groupeUtilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
