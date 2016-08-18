(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutEleveDetailController', StatutEleveDetailController);

    StatutEleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StatutEleve', 'Eleve', 'Statut'];

    function StatutEleveDetailController($scope, $rootScope, $stateParams, previousState, entity, StatutEleve, Eleve, Statut) {
        var vm = this;

        vm.statutEleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:statutEleveUpdate', function(event, result) {
            vm.statutEleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
