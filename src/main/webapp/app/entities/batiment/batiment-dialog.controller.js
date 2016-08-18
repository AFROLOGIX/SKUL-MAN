(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BatimentDialogController', BatimentDialogController);

    BatimentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Batiment'];

    function BatimentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Batiment) {
        var vm = this;

        vm.batiment = entity;
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
            if (vm.batiment.id !== null) {
                Batiment.update(vm.batiment, onSaveSuccess, onSaveError);
            } else {
                Batiment.save(vm.batiment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:batimentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
