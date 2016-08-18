(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('JourDialogController', JourDialogController);

    JourDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Jour'];

    function JourDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Jour) {
        var vm = this;

        vm.jour = entity;
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
            if (vm.jour.id !== null) {
                Jour.update(vm.jour, onSaveSuccess, onSaveError);
            } else {
                Jour.save(vm.jour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:jourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
