(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EtablissementDeleteController',EtablissementDeleteController);

    EtablissementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Etablissement'];

    function EtablissementDeleteController($uibModalInstance, entity, Etablissement) {
        var vm = this;

        vm.etablissement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Etablissement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
