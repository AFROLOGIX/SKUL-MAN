(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PersonnelDialogController', PersonnelDialogController);

    PersonnelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Personnel', 'Utilisateur', 'TypePersonnel', 'AbsencePersonnel'];

    function PersonnelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Personnel, Utilisateur, TypePersonnel, AbsencePersonnel) {
        var vm = this;

        vm.personnel = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.utilisateurs = Utilisateur.query({filter: 'personnel-is-null'});
        $q.all([vm.personnel.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.personnel.utilisateur || !vm.personnel.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.personnel.utilisateur.id}).$promise;
        }).then(function(utilisateur) {
            vm.utilisateurs.push(utilisateur);
        });
        vm.typepersonnels = TypePersonnel.query({filter: 'personnel-is-null'});
        $q.all([vm.personnel.$promise, vm.typepersonnels.$promise]).then(function() {
            if (!vm.personnel.typePersonnel || !vm.personnel.typePersonnel.id) {
                return $q.reject();
            }
            return TypePersonnel.get({id : vm.personnel.typePersonnel.id}).$promise;
        }).then(function(typePersonnel) {
            vm.typepersonnels.push(typePersonnel);
        });
        vm.absencepersonnels = AbsencePersonnel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.personnel.id !== null) {
                Personnel.update(vm.personnel, onSaveSuccess, onSaveError);
            } else {
                Personnel.save(vm.personnel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:personnelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateAdmission = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
