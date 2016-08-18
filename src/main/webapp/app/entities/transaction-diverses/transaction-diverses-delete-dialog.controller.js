(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TransactionDiversesDeleteController',TransactionDiversesDeleteController);

    TransactionDiversesDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransactionDiverses'];

    function TransactionDiversesDeleteController($uibModalInstance, entity, TransactionDiverses) {
        var vm = this;

        vm.transactionDiverses = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransactionDiverses.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
