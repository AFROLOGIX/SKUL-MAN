(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BatimentDetailController', BatimentDetailController);

    BatimentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Batiment'];

    function BatimentDetailController($scope, $rootScope, $stateParams, previousState, entity, Batiment) {
        var vm = this;

        vm.batiment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:batimentUpdate', function(event, result) {
            vm.batiment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
