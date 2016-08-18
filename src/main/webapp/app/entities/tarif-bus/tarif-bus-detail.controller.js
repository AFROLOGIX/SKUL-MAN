(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TarifBusDetailController', TarifBusDetailController);

    TarifBusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TarifBus'];

    function TarifBusDetailController($scope, $rootScope, $stateParams, previousState, entity, TarifBus) {
        var vm = this;

        vm.tarifBus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:tarifBusUpdate', function(event, result) {
            vm.tarifBus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
