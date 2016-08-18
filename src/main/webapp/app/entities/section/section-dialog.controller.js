(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SectionDialogController', SectionDialogController);

    SectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Section', 'Cycle'];

    function SectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Section, Cycle) {
        var vm = this;

        vm.section = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cycles = Cycle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.section.id !== null) {
                Section.update(vm.section, onSaveSuccess, onSaveError);
            } else {
                Section.save(vm.section, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:sectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
