(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoratoireDeleteController',MoratoireDeleteController);

    MoratoireDeleteController.$inject = ['$uibModalInstance', 'entity', 'Moratoire'];

    function MoratoireDeleteController($uibModalInstance, entity, Moratoire) {
        var vm = this;

        vm.moratoire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Moratoire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
