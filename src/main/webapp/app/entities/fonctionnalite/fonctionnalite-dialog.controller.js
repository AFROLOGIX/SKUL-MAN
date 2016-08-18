(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FonctionnaliteDialogController', FonctionnaliteDialogController);

    FonctionnaliteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fonctionnalite'];

    function FonctionnaliteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fonctionnalite) {
        var vm = this;

        vm.fonctionnalite = entity;
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
            if (vm.fonctionnalite.id !== null) {
                Fonctionnalite.update(vm.fonctionnalite, onSaveSuccess, onSaveError);
            } else {
                Fonctionnalite.save(vm.fonctionnalite, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:fonctionnaliteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
