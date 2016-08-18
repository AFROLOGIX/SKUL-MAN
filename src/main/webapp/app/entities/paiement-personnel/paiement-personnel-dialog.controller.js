(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PaiementPersonnelDialogController', PaiementPersonnelDialogController);

    PaiementPersonnelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PaiementPersonnel', 'AgentAdministratif', 'Operation', 'Personnel'];

    function PaiementPersonnelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PaiementPersonnel, AgentAdministratif, Operation, Personnel) {
        var vm = this;

        vm.paiementPersonnel = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.agentadministratifs = AgentAdministratif.query({filter: 'paiementpersonnel-is-null'});
        $q.all([vm.paiementPersonnel.$promise, vm.agentadministratifs.$promise]).then(function() {
            if (!vm.paiementPersonnel.agentAdministratif || !vm.paiementPersonnel.agentAdministratif.id) {
                return $q.reject();
            }
            return AgentAdministratif.get({id : vm.paiementPersonnel.agentAdministratif.id}).$promise;
        }).then(function(agentAdministratif) {
            vm.agentadministratifs.push(agentAdministratif);
        });
        vm.operations = Operation.query({filter: 'paiementpersonnel-is-null'});
        $q.all([vm.paiementPersonnel.$promise, vm.operations.$promise]).then(function() {
            if (!vm.paiementPersonnel.operation || !vm.paiementPersonnel.operation.id) {
                return $q.reject();
            }
            return Operation.get({id : vm.paiementPersonnel.operation.id}).$promise;
        }).then(function(operation) {
            vm.operations.push(operation);
        });
        vm.personnels = Personnel.query({filter: 'paiementpersonnel-is-null'});
        $q.all([vm.paiementPersonnel.$promise, vm.personnels.$promise]).then(function() {
            if (!vm.paiementPersonnel.personnel || !vm.paiementPersonnel.personnel.id) {
                return $q.reject();
            }
            return Personnel.get({id : vm.paiementPersonnel.personnel.id}).$promise;
        }).then(function(personnel) {
            vm.personnels.push(personnel);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paiementPersonnel.id !== null) {
                PaiementPersonnel.update(vm.paiementPersonnel, onSaveSuccess, onSaveError);
            } else {
                PaiementPersonnel.save(vm.paiementPersonnel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:paiementPersonnelUpdate', result);
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
