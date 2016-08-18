(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeOperationDialogController', TypeOperationDialogController);

    TypeOperationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeOperation'];

    function TypeOperationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeOperation) {
        var vm = this;

        vm.typeOperation = entity;
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
            if (vm.typeOperation.id !== null) {
                TypeOperation.update(vm.typeOperation, onSaveSuccess, onSaveError);
            } else {
                TypeOperation.save(vm.typeOperation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typeOperationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
