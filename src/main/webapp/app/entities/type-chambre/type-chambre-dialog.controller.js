(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeChambreDialogController', TypeChambreDialogController);

    TypeChambreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeChambre'];

    function TypeChambreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeChambre) {
        var vm = this;

        vm.typeChambre = entity;
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
            if (vm.typeChambre.id !== null) {
                TypeChambre.update(vm.typeChambre, onSaveSuccess, onSaveError);
            } else {
                TypeChambre.save(vm.typeChambre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typeChambreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
