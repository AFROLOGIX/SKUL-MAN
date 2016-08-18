(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AnneeScolaireDeleteController',AnneeScolaireDeleteController);

    AnneeScolaireDeleteController.$inject = ['$uibModalInstance', 'entity', 'AnneeScolaire'];

    function AnneeScolaireDeleteController($uibModalInstance, entity, AnneeScolaire) {
        var vm = this;

        vm.anneeScolaire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AnneeScolaire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
