(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoyenneTableauHonneurDialogController', MoyenneTableauHonneurDialogController);

    MoyenneTableauHonneurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MoyenneTableauHonneur'];

    function MoyenneTableauHonneurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MoyenneTableauHonneur) {
        var vm = this;

        vm.moyenneTableauHonneur = entity;
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
            if (vm.moyenneTableauHonneur.id !== null) {
                MoyenneTableauHonneur.update(vm.moyenneTableauHonneur, onSaveSuccess, onSaveError);
            } else {
                MoyenneTableauHonneur.save(vm.moyenneTableauHonneur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:moyenneTableauHonneurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
