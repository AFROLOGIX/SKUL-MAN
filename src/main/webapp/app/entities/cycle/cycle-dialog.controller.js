(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CycleDialogController', CycleDialogController);

    CycleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cycle', 'Niveau', 'Section'];

    function CycleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cycle, Niveau, Section) {
        var vm = this;

        vm.cycle = entity;
        vm.clear = clear;
        vm.save = save;
        vm.niveaus = Niveau.query();
        vm.sections = Section.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cycle.id !== null) {
                Cycle.update(vm.cycle, onSaveSuccess, onSaveError);
            } else {
                Cycle.save(vm.cycle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:cycleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
