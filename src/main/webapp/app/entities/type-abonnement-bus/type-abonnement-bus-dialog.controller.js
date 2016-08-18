(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeAbonnementBusDialogController', TypeAbonnementBusDialogController);

    TypeAbonnementBusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeAbonnementBus'];

    function TypeAbonnementBusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeAbonnementBus) {
        var vm = this;

        vm.typeAbonnementBus = entity;
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
            if (vm.typeAbonnementBus.id !== null) {
                TypeAbonnementBus.update(vm.typeAbonnementBus, onSaveSuccess, onSaveError);
            } else {
                TypeAbonnementBus.save(vm.typeAbonnementBus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typeAbonnementBusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
