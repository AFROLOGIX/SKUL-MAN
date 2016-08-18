(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalleDeleteController',SalleDeleteController);

    SalleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Salle'];

    function SalleDeleteController($uibModalInstance, entity, Salle) {
        var vm = this;

        vm.salle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Salle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
