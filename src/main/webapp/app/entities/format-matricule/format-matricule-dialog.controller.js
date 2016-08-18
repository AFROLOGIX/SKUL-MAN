(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FormatMatriculeDialogController', FormatMatriculeDialogController);

    FormatMatriculeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FormatMatricule'];

    function FormatMatriculeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FormatMatricule) {
        var vm = this;

        vm.formatMatricule = entity;
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
            if (vm.formatMatricule.id !== null) {
                FormatMatricule.update(vm.formatMatricule, onSaveSuccess, onSaveError);
            } else {
                FormatMatricule.save(vm.formatMatricule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:formatMatriculeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
