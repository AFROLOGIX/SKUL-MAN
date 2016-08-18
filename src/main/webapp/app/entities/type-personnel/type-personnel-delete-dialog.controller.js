(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypePersonnelDeleteController',TypePersonnelDeleteController);

    TypePersonnelDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypePersonnel'];

    function TypePersonnelDeleteController($uibModalInstance, entity, TypePersonnel) {
        var vm = this;

        vm.typePersonnel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypePersonnel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
