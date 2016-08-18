(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEnseignantDeleteController',AbsenceEnseignantDeleteController);

    AbsenceEnseignantDeleteController.$inject = ['$uibModalInstance', 'entity', 'AbsenceEnseignant'];

    function AbsenceEnseignantDeleteController($uibModalInstance, entity, AbsenceEnseignant) {
        var vm = this;

        vm.absenceEnseignant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AbsenceEnseignant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
