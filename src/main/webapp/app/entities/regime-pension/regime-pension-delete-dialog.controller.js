(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('RegimePensionDeleteController',RegimePensionDeleteController);

    RegimePensionDeleteController.$inject = ['$uibModalInstance', 'entity', 'RegimePension'];

    function RegimePensionDeleteController($uibModalInstance, entity, RegimePension) {
        var vm = this;

        vm.regimePension = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RegimePension.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
