(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PauseDialogController', PauseDialogController);

    PauseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pause'];

    function PauseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pause) {
        var vm = this;

        vm.pause = entity;
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
            if (vm.pause.id !== null) {
                Pause.update(vm.pause, onSaveSuccess, onSaveError);
            } else {
                Pause.save(vm.pause, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:pauseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.heureDeb = false;
        vm.datePickerOpenStatus.heureFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
