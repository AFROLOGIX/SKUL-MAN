(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutDetailController', StatutDetailController);

    StatutDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Statut'];

    function StatutDetailController($scope, $rootScope, $stateParams, previousState, entity, Statut) {
        var vm = this;

        vm.statut = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:statutUpdate', function(event, result) {
            vm.statut = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
