(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MotifExclusionDialogController', MotifExclusionDialogController);

    MotifExclusionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MotifExclusion'];

    function MotifExclusionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MotifExclusion) {
        var vm = this;

        vm.motifExclusion = entity;
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
            if (vm.motifExclusion.id !== null) {
                MotifExclusion.update(vm.motifExclusion, onSaveSuccess, onSaveError);
            } else {
                MotifExclusion.save(vm.motifExclusion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:motifExclusionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
