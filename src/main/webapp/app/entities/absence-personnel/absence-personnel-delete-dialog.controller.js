(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsencePersonnelDeleteController',AbsencePersonnelDeleteController);

    AbsencePersonnelDeleteController.$inject = ['$uibModalInstance', 'entity', 'AbsencePersonnel'];

    function AbsencePersonnelDeleteController($uibModalInstance, entity, AbsencePersonnel) {
        var vm = this;

        vm.absencePersonnel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AbsencePersonnel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
