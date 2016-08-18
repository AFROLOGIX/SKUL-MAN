(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('JourDeleteController',JourDeleteController);

    JourDeleteController.$inject = ['$uibModalInstance', 'entity', 'Jour'];

    function JourDeleteController($uibModalInstance, entity, Jour) {
        var vm = this;

        vm.jour = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Jour.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
