(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutDeleteController',StatutDeleteController);

    StatutDeleteController.$inject = ['$uibModalInstance', 'entity', 'Statut'];

    function StatutDeleteController($uibModalInstance, entity, Statut) {
        var vm = this;

        vm.statut = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Statut.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
