(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TransactionDiversesDetailController', TransactionDiversesDetailController);

    TransactionDiversesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransactionDiverses', 'AgentAdministratif', 'Operation'];

    function TransactionDiversesDetailController($scope, $rootScope, $stateParams, previousState, entity, TransactionDiverses, AgentAdministratif, Operation) {
        var vm = this;

        vm.transactionDiverses = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:transactionDiversesUpdate', function(event, result) {
            vm.transactionDiverses = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
