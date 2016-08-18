(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeMoratoireDeleteController',TypeMoratoireDeleteController);

    TypeMoratoireDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeMoratoire'];

    function TypeMoratoireDeleteController($uibModalInstance, entity, TypeMoratoire) {
        var vm = this;

        vm.typeMoratoire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeMoratoire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
