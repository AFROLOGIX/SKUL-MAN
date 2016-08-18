(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PensionDeleteController',PensionDeleteController);

    PensionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pension'];

    function PensionDeleteController($uibModalInstance, entity, Pension) {
        var vm = this;

        vm.pension = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pension.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
