(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginActionDialogController', LoginActionDialogController);

    LoginActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LoginAction', 'Utilisateur'];

    function LoginActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, LoginAction, Utilisateur) {
        var vm = this;

        vm.loginAction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.utilisateurs = Utilisateur.query({filter: 'loginaction-is-null'});
        $q.all([vm.loginAction.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.loginAction.utilisateur || !vm.loginAction.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.loginAction.utilisateur.id}).$promise;
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
            if (vm.loginAction.id !== null) {
                LoginAction.update(vm.loginAction, onSaveSuccess, onSaveError);
            } else {
                LoginAction.save(vm.loginAction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:loginActionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
