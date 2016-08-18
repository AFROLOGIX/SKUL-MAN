(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeMoratoireDialogController', TypeMoratoireDialogController);

    TypeMoratoireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeMoratoire'];

    function TypeMoratoireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeMoratoire) {
        var vm = this;

        vm.typeMoratoire = entity;
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
            if (vm.typeMoratoire.id !== null) {
                TypeMoratoire.update(vm.typeMoratoire, onSaveSuccess, onSaveError);
            } else {
                TypeMoratoire.save(vm.typeMoratoire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:typeMoratoireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
