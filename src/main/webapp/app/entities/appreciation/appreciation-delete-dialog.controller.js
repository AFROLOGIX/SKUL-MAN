(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AppreciationDeleteController',AppreciationDeleteController);

    AppreciationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Appreciation'];

    function AppreciationDeleteController($uibModalInstance, entity, Appreciation) {
        var vm = this;

        vm.appreciation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Appreciation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
