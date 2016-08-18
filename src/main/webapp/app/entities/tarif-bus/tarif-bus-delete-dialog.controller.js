(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TarifBusDeleteController',TarifBusDeleteController);

    TarifBusDeleteController.$inject = ['$uibModalInstance', 'entity', 'TarifBus'];

    function TarifBusDeleteController($uibModalInstance, entity, TarifBus) {
        var vm = this;

        vm.tarifBus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TarifBus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
