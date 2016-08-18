(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalaireDialogController', SalaireDialogController);

    SalaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Salaire'];

    function SalaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Salaire) {
        var vm = this;

        vm.salaire = entity;
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
            if (vm.salaire.id !== null) {
                Salaire.update(vm.salaire, onSaveSuccess, onSaveError);
            } else {
                Salaire.save(vm.salaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:salaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
