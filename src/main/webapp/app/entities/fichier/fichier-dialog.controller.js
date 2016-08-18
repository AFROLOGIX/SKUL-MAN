(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FichierDialogController', FichierDialogController);

    FichierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fichier', 'AgentAdministratif', 'Eleve', 'Enseignant'];

    function FichierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fichier, AgentAdministratif, Eleve, Enseignant) {
        var vm = this;

        vm.fichier = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.agentadministratifs = AgentAdministratif.query();
        vm.eleves = Eleve.query();
        vm.enseignants = Enseignant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fichier.id !== null) {
                Fichier.update(vm.fichier, onSaveSuccess, onSaveError);
            } else {
                Fichier.save(vm.fichier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:fichierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createAt = false;
        vm.datePickerOpenStatus.updateAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
