(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('JourDetailController', JourDetailController);

    JourDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Jour'];

    function JourDetailController($scope, $rootScope, $stateParams, previousState, entity, Jour) {
        var vm = this;

        vm.jour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:jourUpdate', function(event, result) {
            vm.jour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
