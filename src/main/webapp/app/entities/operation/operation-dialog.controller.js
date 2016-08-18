(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('OperationDialogController', OperationDialogController);

    OperationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Operation', 'TypeOperation'];

    function OperationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Operation, TypeOperation) {
        var vm = this;

        vm.operation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.typeoperations = TypeOperation.query({filter: 'operation-is-null'});
        $q.all([vm.operation.$promise, vm.typeoperations.$promise]).then(function() {
            if (!vm.operation.typeOperation || !vm.operation.typeOperation.id) {
                return $q.reject();
            }
            return TypeOperation.get({id : vm.operation.typeOperation.id}).$promise;
        }).then(function(typeOperation) {
            vm.typeoperations.push(typeOperation);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.operation.id !== null) {
                Operation.update(vm.operation, onSaveSuccess, onSaveError);
            } else {
                Operation.save(vm.operation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:operationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
