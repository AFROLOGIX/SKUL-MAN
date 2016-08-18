(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PaiementPersonnelDeleteController',PaiementPersonnelDeleteController);

    PaiementPersonnelDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaiementPersonnel'];

    function PaiementPersonnelDeleteController($uibModalInstance, entity, PaiementPersonnel) {
        var vm = this;

        vm.paiementPersonnel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaiementPersonnel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
