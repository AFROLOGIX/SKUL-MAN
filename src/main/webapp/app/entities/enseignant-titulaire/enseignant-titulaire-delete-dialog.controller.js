(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantTitulaireDeleteController',EnseignantTitulaireDeleteController);

    EnseignantTitulaireDeleteController.$inject = ['$uibModalInstance', 'entity', 'EnseignantTitulaire'];

    function EnseignantTitulaireDeleteController($uibModalInstance, entity, EnseignantTitulaire) {
        var vm = this;

        vm.enseignantTitulaire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EnseignantTitulaire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
