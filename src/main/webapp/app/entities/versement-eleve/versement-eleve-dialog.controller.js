(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('VersementEleveDialogController', VersementEleveDialogController);

    VersementEleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'VersementEleve', 'AgentAdministratif', 'Eleve', 'Operation'];

    function VersementEleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, VersementEleve, AgentAdministratif, Eleve, Operation) {
        var vm = this;

        vm.versementEleve = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.agentadministratifs = AgentAdministratif.query({filter: 'versementeleve-is-null'});
        $q.all([vm.versementEleve.$promise, vm.agentadministratifs.$promise]).then(function() {
            if (!vm.versementEleve.agentAdministratif || !vm.versementEleve.agentAdministratif.id) {
                return $q.reject();
            }
            return AgentAdministratif.get({id : vm.versementEleve.agentAdministratif.id}).$promise;
        }).then(function(agentAdministratif) {
            vm.agentadministratifs.push(agentAdministratif);
        });
        vm.eleves = Eleve.query({filter: 'versementeleve-is-null'});
        $q.all([vm.versementEleve.$promise, vm.eleves.$promise]).then(function() {
            if (!vm.versementEleve.eleve || !vm.versementEleve.eleve.id) {
                return $q.reject();
            }
            return Eleve.get({id : vm.versementEleve.eleve.id}).$promise;
        }).then(function(eleve) {
            vm.eleves.push(eleve);
        });
        vm.operations = Operation.query({filter: 'versementeleve-is-null'});
        $q.all([vm.versementEleve.$promise, vm.operations.$promise]).then(function() {
            if (!vm.versementEleve.operation || !vm.versementEleve.operation.id) {
                return $q.reject();
            }
            return Operation.get({id : vm.versementEleve.operation.id}).$promise;
        }).then(function(operation) {
            vm.operations.push(operation);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.versementEleve.id !== null) {
                VersementEleve.update(vm.versementEleve, onSaveSuccess, onSaveError);
            } else {
                VersementEleve.save(vm.versementEleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:versementEleveUpdate', result);
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
