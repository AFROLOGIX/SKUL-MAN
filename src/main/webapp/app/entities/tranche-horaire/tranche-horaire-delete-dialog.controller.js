(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheHoraireDeleteController',TrancheHoraireDeleteController);

    TrancheHoraireDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrancheHoraire'];

    function TrancheHoraireDeleteController($uibModalInstance, entity, TrancheHoraire) {
        var vm = this;

        vm.trancheHoraire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrancheHoraire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
