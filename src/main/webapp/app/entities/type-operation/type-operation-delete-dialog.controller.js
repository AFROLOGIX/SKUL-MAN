(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeOperationDeleteController',TypeOperationDeleteController);

    TypeOperationDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeOperation'];

    function TypeOperationDeleteController($uibModalInstance, entity, TypeOperation) {
        var vm = this;

        vm.typeOperation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeOperation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
