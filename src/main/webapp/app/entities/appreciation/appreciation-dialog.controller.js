(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AppreciationDialogController', AppreciationDialogController);

    AppreciationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Appreciation'];

    function AppreciationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Appreciation) {
        var vm = this;

        vm.appreciation = entity;
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
            if (vm.appreciation.id !== null) {
                Appreciation.update(vm.appreciation, onSaveSuccess, onSaveError);
            } else {
                Appreciation.save(vm.appreciation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:appreciationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
