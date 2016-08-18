(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SerieDialogController', SerieDialogController);

    SerieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Serie'];

    function SerieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Serie) {
        var vm = this;

        vm.serie = entity;
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
            if (vm.serie.id !== null) {
                Serie.update(vm.serie, onSaveSuccess, onSaveError);
            } else {
                Serie.save(vm.serie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:serieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
