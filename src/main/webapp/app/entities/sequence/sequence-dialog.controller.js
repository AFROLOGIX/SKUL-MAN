(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SequenceDialogController', SequenceDialogController);

    SequenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sequence', 'Trimestre'];

    function SequenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sequence, Trimestre) {
        var vm = this;

        vm.sequence = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trimestres = Trimestre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sequence.id !== null) {
                Sequence.update(vm.sequence, onSaveSuccess, onSaveError);
            } else {
                Sequence.save(vm.sequence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:sequenceUpdate', result);
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
