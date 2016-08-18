(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PeriodeSaisieNoteDialogController', PeriodeSaisieNoteDialogController);

    PeriodeSaisieNoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PeriodeSaisieNote'];

    function PeriodeSaisieNoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PeriodeSaisieNote) {
        var vm = this;

        vm.periodeSaisieNote = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.periodeSaisieNote.id !== null) {
                PeriodeSaisieNote.update(vm.periodeSaisieNote, onSaveSuccess, onSaveError);
            } else {
                PeriodeSaisieNote.save(vm.periodeSaisieNote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:periodeSaisieNoteUpdate', result);
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
