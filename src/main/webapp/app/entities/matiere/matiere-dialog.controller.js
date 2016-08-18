(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereDialogController', MatiereDialogController);

    MatiereDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Matiere'];

    function MatiereDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Matiere) {
        var vm = this;

        vm.matiere = entity;
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
            if (vm.matiere.id !== null) {
                Matiere.update(vm.matiere, onSaveSuccess, onSaveError);
            } else {
                Matiere.save(vm.matiere, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:matiereUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
