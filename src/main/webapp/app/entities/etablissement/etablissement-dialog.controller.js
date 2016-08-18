(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EtablissementDialogController', EtablissementDialogController);

    EtablissementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Etablissement'];

    function EtablissementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Etablissement) {
        var vm = this;

        vm.etablissement = entity;
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
            if (vm.etablissement.id !== null) {
                Etablissement.update(vm.etablissement, onSaveSuccess, onSaveError);
            } else {
                Etablissement.save(vm.etablissement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:etablissementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreation = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
