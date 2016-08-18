(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EvenementDeleteController',EvenementDeleteController);

    EvenementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Evenement'];

    function EvenementDeleteController($uibModalInstance, entity, Evenement) {
        var vm = this;

        vm.evenement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Evenement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
