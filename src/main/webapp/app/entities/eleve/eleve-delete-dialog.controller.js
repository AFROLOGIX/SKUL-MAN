(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EleveDeleteController',EleveDeleteController);

    EleveDeleteController.$inject = ['$uibModalInstance', 'entity', 'Eleve'];

    function EleveDeleteController($uibModalInstance, entity, Eleve) {
        var vm = this;

        vm.eleve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Eleve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
