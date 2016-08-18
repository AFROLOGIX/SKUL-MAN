(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrimestreDeleteController',TrimestreDeleteController);

    TrimestreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Trimestre'];

    function TrimestreDeleteController($uibModalInstance, entity, Trimestre) {
        var vm = this;

        vm.trimestre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Trimestre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
