(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParametreEtablissementDialogController', ParametreEtablissementDialogController);

    ParametreEtablissementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ParametreEtablissement'];

    function ParametreEtablissementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ParametreEtablissement) {
        var vm = this;

        vm.parametreEtablissement = entity;
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
            if (vm.parametreEtablissement.id !== null) {
                ParametreEtablissement.update(vm.parametreEtablissement, onSaveSuccess, onSaveError);
            } else {
                ParametreEtablissement.save(vm.parametreEtablissement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:parametreEtablissementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.heureDebCours = false;
        vm.datePickerOpenStatus.heureFinCours = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
