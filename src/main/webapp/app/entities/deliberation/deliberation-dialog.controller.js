(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DeliberationDialogController', DeliberationDialogController);

    DeliberationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Deliberation', 'Eleve', 'AgentAdministratif', 'Enseignant'];

    function DeliberationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Deliberation, Eleve, AgentAdministratif, Enseignant) {
        var vm = this;

        vm.deliberation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.eleves = Eleve.query({filter: 'deliberation-is-null'});
        $q.all([vm.deliberation.$promise, vm.eleves.$promise]).then(function() {
            if (!vm.deliberation.eleve || !vm.deliberation.eleve.id) {
                return $q.reject();
            }
            return Eleve.get({id : vm.deliberation.eleve.id}).$promise;
        }).then(function(eleve) {
            vm.eleves.push(eleve);
        });
        vm.agentadministratifs = AgentAdministratif.query();
        vm.enseignants = Enseignant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.deliberation.id !== null) {
                Deliberation.update(vm.deliberation, onSaveSuccess, onSaveError);
            } else {
                Deliberation.save(vm.deliberation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:deliberationUpdate', result);
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
