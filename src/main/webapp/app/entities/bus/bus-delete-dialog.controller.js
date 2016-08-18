(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BusDeleteController',BusDeleteController);

    BusDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bus'];

    function BusDeleteController($uibModalInstance, entity, Bus) {
        var vm = this;

        vm.bus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
