(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AppreciationDetailController', AppreciationDetailController);

    AppreciationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Appreciation'];

    function AppreciationDetailController($scope, $rootScope, $stateParams, previousState, entity, Appreciation) {
        var vm = this;

        vm.appreciation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:appreciationUpdate', function(event, result) {
            vm.appreciation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
