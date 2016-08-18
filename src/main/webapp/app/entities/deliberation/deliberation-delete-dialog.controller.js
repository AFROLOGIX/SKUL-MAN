(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DeliberationDeleteController',DeliberationDeleteController);

    DeliberationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deliberation'];

    function DeliberationDeleteController($uibModalInstance, entity, Deliberation) {
        var vm = this;

        vm.deliberation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Deliberation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
