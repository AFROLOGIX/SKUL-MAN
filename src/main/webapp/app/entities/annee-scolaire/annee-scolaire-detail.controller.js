(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AnneeScolaireDetailController', AnneeScolaireDetailController);

    AnneeScolaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AnneeScolaire'];

    function AnneeScolaireDetailController($scope, $rootScope, $stateParams, previousState, entity, AnneeScolaire) {
        var vm = this;

        vm.anneeScolaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:anneeScolaireUpdate', function(event, result) {
            vm.anneeScolaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
