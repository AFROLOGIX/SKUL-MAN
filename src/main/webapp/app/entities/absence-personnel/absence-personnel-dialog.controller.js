(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsencePersonnelDialogController', AbsencePersonnelDialogController);

    AbsencePersonnelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AbsencePersonnel', 'Jour', 'Personnel'];

    function AbsencePersonnelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AbsencePersonnel, Jour, Personnel) {
        var vm = this;

        vm.absencePersonnel = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.jours = Jour.query({filter: 'absencepersonnel-is-null'});
        $q.all([vm.absencePersonnel.$promise, vm.jours.$promise]).then(function() {
            if (!vm.absencePersonnel.jour || !vm.absencePersonnel.jour.id) {
                return $q.reject();
            }
            return Jour.get({id : vm.absencePersonnel.jour.id}).$promise;
        }).then(function(jour) {
            vm.jours.push(jour);
        });
        vm.personnels = Personnel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.absencePersonnel.id !== null) {
                AbsencePersonnel.update(vm.absencePersonnel, onSaveSuccess, onSaveError);
            } else {
                AbsencePersonnel.save(vm.absencePersonnel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:absencePersonnelUpdate', result);
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
