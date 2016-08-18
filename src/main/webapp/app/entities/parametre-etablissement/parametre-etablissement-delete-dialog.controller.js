(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParametreEtablissementDeleteController',ParametreEtablissementDeleteController);

    ParametreEtablissementDeleteController.$inject = ['$uibModalInstance', 'entity', 'ParametreEtablissement'];

    function ParametreEtablissementDeleteController($uibModalInstance, entity, ParametreEtablissement) {
        var vm = this;

        vm.parametreEtablissement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ParametreEtablissement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
