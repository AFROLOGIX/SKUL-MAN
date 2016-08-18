(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('UtilisateurDialogController', UtilisateurDialogController);

    UtilisateurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Utilisateur', 'GroupeUtilisateur'];

    function UtilisateurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Utilisateur, GroupeUtilisateur) {
        var vm = this;

        vm.utilisateur = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.groupeutilisateurs = GroupeUtilisateur.query({filter: 'utilisateur-is-null'});
        $q.all([vm.utilisateur.$promise, vm.groupeutilisateurs.$promise]).then(function() {
            if (!vm.utilisateur.groupeUtilisateur || !vm.utilisateur.groupeUtilisateur.id) {
                return $q.reject();
            }
            return GroupeUtilisateur.get({id : vm.utilisateur.groupeUtilisateur.id}).$promise;
        }).then(function(groupeUtilisateur) {
            vm.groupeutilisateurs.push(groupeUtilisateur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.utilisateur.id !== null) {
                Utilisateur.update(vm.utilisateur, onSaveSuccess, onSaveError);
            } else {
                Utilisateur.save(vm.utilisateur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:utilisateurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateConnexion = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
