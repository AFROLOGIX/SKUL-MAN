(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ReligionDialogController', ReligionDialogController);

    ReligionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Religion'];

    function ReligionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Religion) {
        var vm = this;

        vm.religion = entity;
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
            if (vm.religion.id !== null) {
                Religion.update(vm.religion, onSaveSuccess, onSaveError);
            } else {
                Religion.save(vm.religion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:religionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
