(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypePersonnelDialogController', TypePersonnelDialogController);

    TypePersonnelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypePersonnel'];

    function TypePersonnelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypePersonnel) {
        var vm = this;

        vm.typePersonnel = entity;
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
            if (vm.typePersonnel.id !== null) {
                TypePersonnel.update(vm.typePersonnel, onSaveSuccess, onSaveError);
            } else {
                TypePersonnel.save(vm.typePersonnel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typePersonnelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
