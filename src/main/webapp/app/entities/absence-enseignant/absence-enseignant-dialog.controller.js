(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEnseignantDialogController', AbsenceEnseignantDialogController);

    AbsenceEnseignantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AbsenceEnseignant', 'Jour', 'Enseignant'];

    function AbsenceEnseignantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AbsenceEnseignant, Jour, Enseignant) {
        var vm = this;

        vm.absenceEnseignant = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.jours = Jour.query({filter: 'absenceenseignant-is-null'});
        $q.all([vm.absenceEnseignant.$promise, vm.jours.$promise]).then(function() {
            if (!vm.absenceEnseignant.jour || !vm.absenceEnseignant.jour.id) {
                return $q.reject();
            }
            return Jour.get({id : vm.absenceEnseignant.jour.id}).$promise;
        }).then(function(jour) {
            vm.jours.push(jour);
        });
        vm.enseignants = Enseignant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.absenceEnseignant.id !== null) {
                AbsenceEnseignant.update(vm.absenceEnseignant, onSaveSuccess, onSaveError);
            } else {
                AbsenceEnseignant.save(vm.absenceEnseignant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:absenceEnseignantUpdate', result);
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
