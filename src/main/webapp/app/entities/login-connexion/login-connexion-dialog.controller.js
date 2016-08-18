(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginConnexionDialogController', LoginConnexionDialogController);

    LoginConnexionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LoginConnexion', 'Utilisateur'];

    function LoginConnexionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, LoginConnexion, Utilisateur) {
        var vm = this;

        vm.loginConnexion = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.utilisateurs = Utilisateur.query({filter: 'loginconnexion-is-null'});
        $q.all([vm.loginConnexion.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.loginConnexion.utilisateur || !vm.loginConnexion.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.loginConnexion.utilisateur.id}).$promise;
        }).then(function(utilisateur) {
            vm.utilisateurs.push(utilisateur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.loginConnexion.id !== null) {
                LoginConnexion.update(vm.loginConnexion, onSaveSuccess, onSaveError);
            } else {
                LoginConnexion.save(vm.loginConnexion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:loginConnexionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.loginTime = false;
        vm.datePickerOpenStatus.dateEchec = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
