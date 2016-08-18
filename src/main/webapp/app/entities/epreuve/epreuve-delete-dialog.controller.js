(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EpreuveDeleteController',EpreuveDeleteController);

    EpreuveDeleteController.$inject = ['$uibModalInstance', 'entity', 'Epreuve'];

    function EpreuveDeleteController($uibModalInstance, entity, Epreuve) {
        var vm = this;

        vm.epreuve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Epreuve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
