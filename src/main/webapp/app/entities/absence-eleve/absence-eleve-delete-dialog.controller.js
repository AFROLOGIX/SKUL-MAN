(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEleveDeleteController',AbsenceEleveDeleteController);

    AbsenceEleveDeleteController.$inject = ['$uibModalInstance', 'entity', 'AbsenceEleve'];

    function AbsenceEleveDeleteController($uibModalInstance, entity, AbsenceEleve) {
        var vm = this;

        vm.absenceEleve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AbsenceEleve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
