(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheDialogController', TrancheDialogController);

    TrancheDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tranche', 'RegimePension'];

    function TrancheDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tranche, RegimePension) {
        var vm = this;

        vm.tranche = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.regimepensions = RegimePension.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tranche.id !== null) {
                Tranche.update(vm.tranche, onSaveSuccess, onSaveError);
            } else {
                Tranche.save(vm.tranche, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:trancheUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDeb = false;
        vm.datePickerOpenStatus.dateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
