(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CycleDeleteController',CycleDeleteController);

    CycleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cycle'];

    function CycleDeleteController($uibModalInstance, entity, Cycle) {
        var vm = this;

        vm.cycle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cycle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
