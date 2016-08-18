(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeTrancheHoraireDialogController', TypeTrancheHoraireDialogController);

    TypeTrancheHoraireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeTrancheHoraire'];

    function TypeTrancheHoraireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeTrancheHoraire) {
        var vm = this;

        vm.typeTrancheHoraire = entity;
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
            if (vm.typeTrancheHoraire.id !== null) {
                TypeTrancheHoraire.update(vm.typeTrancheHoraire, onSaveSuccess, onSaveError);
            } else {
                TypeTrancheHoraire.save(vm.typeTrancheHoraire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typeTrancheHoraireUpdate', result);
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
