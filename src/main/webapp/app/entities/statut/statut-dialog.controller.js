(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutDialogController', StatutDialogController);

    StatutDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Statut'];

    function StatutDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Statut) {
        var vm = this;

        vm.statut = entity;
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
            if (vm.statut.id !== null) {
                Statut.update(vm.statut, onSaveSuccess, onSaveError);
            } else {
                Statut.save(vm.statut, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:statutUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
