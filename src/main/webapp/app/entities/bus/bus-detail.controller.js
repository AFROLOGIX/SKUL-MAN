(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BusDetailController', BusDetailController);

    BusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bus', 'TarifBus'];

    function BusDetailController($scope, $rootScope, $stateParams, previousState, entity, Bus, TarifBus) {
        var vm = this;

        vm.bus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:busUpdate', function(event, result) {
            vm.bus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
