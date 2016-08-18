(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalaireDetailController', SalaireDetailController);

    SalaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Salaire'];

    function SalaireDetailController($scope, $rootScope, $stateParams, previousState, entity, Salaire) {
        var vm = this;

        vm.salaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:salaireUpdate', function(event, result) {
            vm.salaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
