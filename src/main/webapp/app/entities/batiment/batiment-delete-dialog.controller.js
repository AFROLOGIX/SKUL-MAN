(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BatimentDeleteController',BatimentDeleteController);

    BatimentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Batiment'];

    function BatimentDeleteController($uibModalInstance, entity, Batiment) {
        var vm = this;

        vm.batiment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Batiment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
