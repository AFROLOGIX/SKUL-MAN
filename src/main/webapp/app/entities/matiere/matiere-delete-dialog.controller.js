(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereDeleteController',MatiereDeleteController);

    MatiereDeleteController.$inject = ['$uibModalInstance', 'entity', 'Matiere'];

    function MatiereDeleteController($uibModalInstance, entity, Matiere) {
        var vm = this;

        vm.matiere = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Matiere.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
