(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TransactionDiversesDialogController', TransactionDiversesDialogController);

    TransactionDiversesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'TransactionDiverses', 'AgentAdministratif', 'Operation'];

    function TransactionDiversesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, TransactionDiverses, AgentAdministratif, Operation) {
        var vm = this;

        vm.transactionDiverses = entity;
        vm.clear = clear;
        vm.save = save;
        vm.agentadministratifs = AgentAdministratif.query({filter: 'transactiondiverses-is-null'});
        $q.all([vm.transactionDiverses.$promise, vm.agentadministratifs.$promise]).then(function() {
            if (!vm.transactionDiverses.agentAdministratif || !vm.transactionDiverses.agentAdministratif.id) {
                return $q.reject();
            }
            return AgentAdministratif.get({id : vm.transactionDiverses.agentAdministratif.id}).$promise;
        }).then(function(agentAdministratif) {
            vm.agentadministratifs.push(agentAdministratif);
        });
        vm.operations = Operation.query({filter: 'transactiondiverses-is-null'});
        $q.all([vm.transactionDiverses.$promise, vm.operations.$promise]).then(function() {
            if (!vm.transactionDiverses.operation || !vm.transactionDiverses.operation.id) {
                return $q.reject();
            }
            return Operation.get({id : vm.transactionDiverses.operation.id}).$promise;
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
            if (vm.transactionDiverses.id !== null) {
                TransactionDiverses.update(vm.transactionDiverses, onSaveSuccess, onSaveError);
            } else {
                TransactionDiverses.save(vm.transactionDiverses, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:transactionDiversesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
