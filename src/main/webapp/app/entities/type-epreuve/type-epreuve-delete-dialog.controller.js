(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeEpreuveDeleteController',TypeEpreuveDeleteController);

    TypeEpreuveDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeEpreuve'];

    function TypeEpreuveDeleteController($uibModalInstance, entity, TypeEpreuve) {
        var vm = this;

        vm.typeEpreuve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeEpreuve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
