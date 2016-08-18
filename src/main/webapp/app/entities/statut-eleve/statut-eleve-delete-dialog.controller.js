(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutEleveDeleteController',StatutEleveDeleteController);

    StatutEleveDeleteController.$inject = ['$uibModalInstance', 'entity', 'StatutEleve'];

    function StatutEleveDeleteController($uibModalInstance, entity, StatutEleve) {
        var vm = this;

        vm.statutEleve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StatutEleve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
