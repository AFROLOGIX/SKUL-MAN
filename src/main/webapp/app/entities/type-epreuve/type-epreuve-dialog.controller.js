(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeEpreuveDialogController', TypeEpreuveDialogController);

    TypeEpreuveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeEpreuve'];

    function TypeEpreuveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeEpreuve) {
        var vm = this;

        vm.typeEpreuve = entity;
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
            if (vm.typeEpreuve.id !== null) {
                TypeEpreuve.update(vm.typeEpreuve, onSaveSuccess, onSaveError);
            } else {
                TypeEpreuve.save(vm.typeEpreuve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typeEpreuveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
