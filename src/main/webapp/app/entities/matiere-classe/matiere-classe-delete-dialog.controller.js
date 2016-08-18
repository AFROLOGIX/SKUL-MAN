(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereClasseDeleteController',MatiereClasseDeleteController);

    MatiereClasseDeleteController.$inject = ['$uibModalInstance', 'entity', 'MatiereClasse'];

    function MatiereClasseDeleteController($uibModalInstance, entity, MatiereClasse) {
        var vm = this;

        vm.matiereClasse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MatiereClasse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
