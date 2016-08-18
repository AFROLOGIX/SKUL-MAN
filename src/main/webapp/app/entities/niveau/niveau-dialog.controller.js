(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NiveauDialogController', NiveauDialogController);

    NiveauDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Niveau', 'Classe', 'Cycle'];

    function NiveauDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Niveau, Classe, Cycle) {
        var vm = this;

        vm.niveau = entity;
        vm.clear = clear;
        vm.save = save;
        vm.classes = Classe.query();
        vm.cycles = Cycle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.niveau.id !== null) {
                Niveau.update(vm.niveau, onSaveSuccess, onSaveError);
            } else {
                Niveau.save(vm.niveau, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:niveauUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
