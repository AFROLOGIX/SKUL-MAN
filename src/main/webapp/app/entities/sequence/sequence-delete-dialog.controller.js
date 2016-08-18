(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SequenceDeleteController',SequenceDeleteController);

    SequenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sequence'];

    function SequenceDeleteController($uibModalInstance, entity, Sequence) {
        var vm = this;

        vm.sequence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sequence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
