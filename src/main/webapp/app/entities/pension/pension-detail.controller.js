(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PensionDetailController', PensionDetailController);

    PensionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pension'];

    function PensionDetailController($scope, $rootScope, $stateParams, previousState, entity, Pension) {
        var vm = this;

        vm.pension = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:pensionUpdate', function(event, result) {
            vm.pension = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
