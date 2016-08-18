(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheDeleteController',TrancheDeleteController);

    TrancheDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tranche'];

    function TrancheDeleteController($uibModalInstance, entity, Tranche) {
        var vm = this;

        vm.tranche = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tranche.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
