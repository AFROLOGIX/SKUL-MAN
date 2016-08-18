(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DroitDeleteController',DroitDeleteController);

    DroitDeleteController.$inject = ['$uibModalInstance', 'entity', 'Droit'];

    function DroitDeleteController($uibModalInstance, entity, Droit) {
        var vm = this;

        vm.droit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Droit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
