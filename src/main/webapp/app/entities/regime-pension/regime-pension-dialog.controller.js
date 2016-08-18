(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('RegimePensionDialogController', RegimePensionDialogController);

    RegimePensionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RegimePension', 'Tranche'];

    function RegimePensionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RegimePension, Tranche) {
        var vm = this;

        vm.regimePension = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tranches = Tranche.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.regimePension.id !== null) {
                RegimePension.update(vm.regimePension, onSaveSuccess, onSaveError);
            } else {
                RegimePension.save(vm.regimePension, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:regimePensionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
