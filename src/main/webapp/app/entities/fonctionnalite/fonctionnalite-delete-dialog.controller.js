(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FonctionnaliteDeleteController',FonctionnaliteDeleteController);

    FonctionnaliteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fonctionnalite'];

    function FonctionnaliteDeleteController($uibModalInstance, entity, Fonctionnalite) {
        var vm = this;

        vm.fonctionnalite = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fonctionnalite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
