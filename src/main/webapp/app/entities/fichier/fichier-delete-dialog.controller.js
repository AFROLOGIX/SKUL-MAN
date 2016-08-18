(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FichierDeleteController',FichierDeleteController);

    FichierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fichier'];

    function FichierDeleteController($uibModalInstance, entity, Fichier) {
        var vm = this;

        vm.fichier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fichier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
