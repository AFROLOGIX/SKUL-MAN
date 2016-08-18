(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeAbonnementBusDeleteController',TypeAbonnementBusDeleteController);

    TypeAbonnementBusDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeAbonnementBus'];

    function TypeAbonnementBusDeleteController($uibModalInstance, entity, TypeAbonnementBus) {
        var vm = this;

        vm.typeAbonnementBus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeAbonnementBus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
