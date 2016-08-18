(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeChambreDeleteController',TypeChambreDeleteController);

    TypeChambreDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeChambre'];

    function TypeChambreDeleteController($uibModalInstance, entity, TypeChambre) {
        var vm = this;

        vm.typeChambre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeChambre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
