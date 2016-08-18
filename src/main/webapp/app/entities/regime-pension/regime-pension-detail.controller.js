(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('RegimePensionDetailController', RegimePensionDetailController);

    RegimePensionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RegimePension', 'Tranche'];

    function RegimePensionDetailController($scope, $rootScope, $stateParams, previousState, entity, RegimePension, Tranche) {
        var vm = this;

        vm.regimePension = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:regimePensionUpdate', function(event, result) {
            vm.regimePension = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
