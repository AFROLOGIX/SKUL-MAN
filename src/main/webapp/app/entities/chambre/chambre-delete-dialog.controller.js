(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreDeleteController',ChambreDeleteController);

    ChambreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Chambre'];

    function ChambreDeleteController($uibModalInstance, entity, Chambre) {
        var vm = this;

        vm.chambre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Chambre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
