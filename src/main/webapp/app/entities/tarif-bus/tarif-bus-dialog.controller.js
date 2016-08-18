(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TarifBusDialogController', TarifBusDialogController);

    TarifBusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TarifBus'];

    function TarifBusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TarifBus) {
        var vm = this;

        vm.tarifBus = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tarifBus.id !== null) {
                TarifBus.update(vm.tarifBus, onSaveSuccess, onSaveError);
            } else {
                TarifBus.save(vm.tarifBus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:tarifBusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
