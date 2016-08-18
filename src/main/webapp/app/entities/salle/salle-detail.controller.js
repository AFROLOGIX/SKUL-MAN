(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalleDetailController', SalleDetailController);

    SalleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Salle'];

    function SalleDetailController($scope, $rootScope, $stateParams, previousState, entity, Salle) {
        var vm = this;

        vm.salle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:salleUpdate', function(event, result) {
            vm.salle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
