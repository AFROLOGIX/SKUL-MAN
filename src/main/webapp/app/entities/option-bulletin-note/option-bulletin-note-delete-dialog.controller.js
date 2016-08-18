(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('OptionBulletinNoteDeleteController',OptionBulletinNoteDeleteController);

    OptionBulletinNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'OptionBulletinNote'];

    function OptionBulletinNoteDeleteController($uibModalInstance, entity, OptionBulletinNote) {
        var vm = this;

        vm.optionBulletinNote = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OptionBulletinNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
