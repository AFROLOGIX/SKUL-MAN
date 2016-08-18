(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreEleveDeleteController',ChambreEleveDeleteController);

    ChambreEleveDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChambreEleve'];

    function ChambreEleveDeleteController($uibModalInstance, entity, ChambreEleve) {
        var vm = this;

        vm.chambreEleve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChambreEleve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
