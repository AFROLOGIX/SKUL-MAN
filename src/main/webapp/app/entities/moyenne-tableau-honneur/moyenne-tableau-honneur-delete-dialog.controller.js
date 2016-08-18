(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoyenneTableauHonneurDeleteController',MoyenneTableauHonneurDeleteController);

    MoyenneTableauHonneurDeleteController.$inject = ['$uibModalInstance', 'entity', 'MoyenneTableauHonneur'];

    function MoyenneTableauHonneurDeleteController($uibModalInstance, entity, MoyenneTableauHonneur) {
        var vm = this;

        vm.moyenneTableauHonneur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MoyenneTableauHonneur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
