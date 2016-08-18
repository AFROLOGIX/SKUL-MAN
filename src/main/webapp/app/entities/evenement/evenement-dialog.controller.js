(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EvenementDialogController', EvenementDialogController);

    EvenementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Evenement'];

    function EvenementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Evenement) {
        var vm = this;

        vm.evenement = entity;
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
            if (vm.evenement.id !== null) {
                Evenement.update(vm.evenement, onSaveSuccess, onSaveError);
            } else {
                Evenement.save(vm.evenement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:evenementUpdate', result);
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
