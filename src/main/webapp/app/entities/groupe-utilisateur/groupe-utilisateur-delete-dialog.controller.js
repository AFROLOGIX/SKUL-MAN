(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('GroupeUtilisateurDeleteController',GroupeUtilisateurDeleteController);

    GroupeUtilisateurDeleteController.$inject = ['$uibModalInstance', 'entity', 'GroupeUtilisateur'];

    function GroupeUtilisateurDeleteController($uibModalInstance, entity, GroupeUtilisateur) {
        var vm = this;

        vm.groupeUtilisateur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GroupeUtilisateur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
