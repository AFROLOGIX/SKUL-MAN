(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheDetailController', TrancheDetailController);

    TrancheDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tranche', 'RegimePension'];

    function TrancheDetailController($scope, $rootScope, $stateParams, previousState, entity, Tranche, RegimePension) {
        var vm = this;

        vm.tranche = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:trancheUpdate', function(event, result) {
            vm.tranche = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
