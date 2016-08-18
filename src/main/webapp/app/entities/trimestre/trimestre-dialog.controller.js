(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrimestreDialogController', TrimestreDialogController);

    TrimestreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Trimestre', 'Sequence'];

    function TrimestreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Trimestre, Sequence) {
        var vm = this;

        vm.trimestre = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sequences = Sequence.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trimestre.id !== null) {
                Trimestre.update(vm.trimestre, onSaveSuccess, onSaveError);
            } else {
                Trimestre.save(vm.trimestre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:trimestreUpdate', result);
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
