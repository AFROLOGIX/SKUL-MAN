(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('GroupeUtilisateurDialogController', GroupeUtilisateurDialogController);

    GroupeUtilisateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GroupeUtilisateur'];

    function GroupeUtilisateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GroupeUtilisateur) {
        var vm = this;

        vm.groupeUtilisateur = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.groupeUtilisateur.id !== null) {
                GroupeUtilisateur.update(vm.groupeUtilisateur, onSaveSuccess, onSaveError);
            } else {
                GroupeUtilisateur.save(vm.groupeUtilisateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:groupeUtilisateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
