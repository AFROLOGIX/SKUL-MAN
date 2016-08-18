(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeTrancheHoraireDeleteController',TypeTrancheHoraireDeleteController);

    TypeTrancheHoraireDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeTrancheHoraire'];

    function TypeTrancheHoraireDeleteController($uibModalInstance, entity, TypeTrancheHoraire) {
        var vm = this;

        vm.typeTrancheHoraire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeTrancheHoraire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
