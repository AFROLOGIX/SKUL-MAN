(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DroitDialogController', DroitDialogController);

    DroitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Droit', 'Fonctionnalite', 'GroupeUtilisateur'];

    function DroitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Droit, Fonctionnalite, GroupeUtilisateur) {
        var vm = this;

        vm.droit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fonctionnalites = Fonctionnalite.query({filter: 'droit-is-null'});
        $q.all([vm.droit.$promise, vm.fonctionnalites.$promise]).then(function() {
            if (!vm.droit.fonctionnalite || !vm.droit.fonctionnalite.id) {
                return $q.reject();
            }
            return Fonctionnalite.get({id : vm.droit.fonctionnalite.id}).$promise;
        }).then(function(fonctionnalite) {
            vm.fonctionnalites.push(fonctionnalite);
        });
        vm.groupeutilisateurs = GroupeUtilisateur.query({filter: 'droit-is-null'});
        $q.all([vm.droit.$promise, vm.groupeutilisateurs.$promise]).then(function() {
            if (!vm.droit.groupeUtilisateur || !vm.droit.groupeUtilisateur.id) {
                return $q.reject();
            }
            return GroupeUtilisateur.get({id : vm.droit.groupeUtilisateur.id}).$promise;
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
            if (vm.droit.id !== null) {
                Droit.update(vm.droit, onSaveSuccess, onSaveError);
            } else {
                Droit.save(vm.droit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:droitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
