(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeAbonnementBusDetailController', TypeAbonnementBusDetailController);

    TypeAbonnementBusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeAbonnementBus'];

    function TypeAbonnementBusDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeAbonnementBus) {
        var vm = this;

        vm.typeAbonnementBus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typeAbonnementBusUpdate', function(event, result) {
            vm.typeAbonnementBus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
