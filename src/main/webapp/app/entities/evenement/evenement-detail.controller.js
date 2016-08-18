(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EvenementDetailController', EvenementDetailController);

    EvenementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Evenement'];

    function EvenementDetailController($scope, $rootScope, $stateParams, previousState, entity, Evenement) {
        var vm = this;

        vm.evenement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:evenementUpdate', function(event, result) {
            vm.evenement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
