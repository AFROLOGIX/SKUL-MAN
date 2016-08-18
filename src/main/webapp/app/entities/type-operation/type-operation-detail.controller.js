(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeOperationDetailController', TypeOperationDetailController);

    TypeOperationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeOperation'];

    function TypeOperationDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeOperation) {
        var vm = this;

        vm.typeOperation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typeOperationUpdate', function(event, result) {
            vm.typeOperation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
