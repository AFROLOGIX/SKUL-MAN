(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEleveDialogController', AbsenceEleveDialogController);

    AbsenceEleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AbsenceEleve', 'Jour', 'Eleve'];

    function AbsenceEleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AbsenceEleve, Jour, Eleve) {
        var vm = this;

        vm.absenceEleve = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.jours = Jour.query({filter: 'absenceeleve-is-null'});
        $q.all([vm.absenceEleve.$promise, vm.jours.$promise]).then(function() {
            if (!vm.absenceEleve.jour || !vm.absenceEleve.jour.id) {
                return $q.reject();
            }
            return Jour.get({id : vm.absenceEleve.jour.id}).$promise;
        }).then(function(jour) {
            vm.jours.push(jour);
        });
        vm.eleves = Eleve.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.absenceEleve.id !== null) {
                AbsenceEleve.update(vm.absenceEleve, onSaveSuccess, onSaveError);
            } else {
                AbsenceEleve.save(vm.absenceEleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:absenceEleveUpdate', result);
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
