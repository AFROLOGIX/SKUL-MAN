(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AnneeScolaireDialogController', AnneeScolaireDialogController);

    AnneeScolaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AnneeScolaire'];

    function AnneeScolaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AnneeScolaire) {
        var vm = this;

        vm.anneeScolaire = entity;
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
            if (vm.anneeScolaire.id !== null) {
                AnneeScolaire.update(vm.anneeScolaire, onSaveSuccess, onSaveError);
            } else {
                AnneeScolaire.save(vm.anneeScolaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:anneeScolaireUpdate', result);
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
