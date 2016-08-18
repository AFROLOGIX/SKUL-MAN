(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PeriodeSaisieNoteDeleteController',PeriodeSaisieNoteDeleteController);

    PeriodeSaisieNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'PeriodeSaisieNote'];

    function PeriodeSaisieNoteDeleteController($uibModalInstance, entity, PeriodeSaisieNote) {
        var vm = this;

        vm.periodeSaisieNote = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PeriodeSaisieNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
