(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BourseDialogController', BourseDialogController);

    BourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bourse', 'Eleve'];

    function BourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bourse, Eleve) {
        var vm = this;

        vm.bourse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.eleves = Eleve.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bourse.id !== null) {
                Bourse.update(vm.bourse, onSaveSuccess, onSaveError);
            } else {
                Bourse.save(vm.bourse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:bourseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
