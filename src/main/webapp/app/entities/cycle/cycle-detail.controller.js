(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CycleDetailController', CycleDetailController);

    CycleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cycle', 'Niveau', 'Section'];

    function CycleDetailController($scope, $rootScope, $stateParams, previousState, entity, Cycle, Niveau, Section) {
        var vm = this;

        vm.cycle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:cycleUpdate', function(event, result) {
            vm.cycle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
