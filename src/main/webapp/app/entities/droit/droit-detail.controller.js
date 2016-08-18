(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DroitDetailController', DroitDetailController);

    DroitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Droit', 'Fonctionnalite', 'GroupeUtilisateur'];

    function DroitDetailController($scope, $rootScope, $stateParams, previousState, entity, Droit, Fonctionnalite, GroupeUtilisateur) {
        var vm = this;

        vm.droit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:droitUpdate', function(event, result) {
            vm.droit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
