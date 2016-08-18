(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('OptionBulletinNoteDialogController', OptionBulletinNoteDialogController);

    OptionBulletinNoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OptionBulletinNote'];

    function OptionBulletinNoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OptionBulletinNote) {
        var vm = this;

        vm.optionBulletinNote = entity;
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
            if (vm.optionBulletinNote.id !== null) {
                OptionBulletinNote.update(vm.optionBulletinNote, onSaveSuccess, onSaveError);
            } else {
                OptionBulletinNote.save(vm.optionBulletinNote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:optionBulletinNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
