(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PauseDeleteController',PauseDeleteController);

    PauseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pause'];

    function PauseDeleteController($uibModalInstance, entity, Pause) {
        var vm = this;

        vm.pause = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pause.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
