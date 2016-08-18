(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NiveauDeleteController',NiveauDeleteController);

    NiveauDeleteController.$inject = ['$uibModalInstance', 'entity', 'Niveau'];

    function NiveauDeleteController($uibModalInstance, entity, Niveau) {
        var vm = this;

        vm.niveau = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Niveau.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
