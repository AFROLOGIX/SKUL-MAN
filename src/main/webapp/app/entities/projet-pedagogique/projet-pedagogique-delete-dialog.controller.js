(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ProjetPedagogiqueDeleteController',ProjetPedagogiqueDeleteController);

    ProjetPedagogiqueDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjetPedagogique'];

    function ProjetPedagogiqueDeleteController($uibModalInstance, entity, ProjetPedagogique) {
        var vm = this;

        vm.projetPedagogique = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjetPedagogique.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
