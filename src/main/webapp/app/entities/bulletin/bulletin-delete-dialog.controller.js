(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BulletinDeleteController',BulletinDeleteController);

    BulletinDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bulletin'];

    function BulletinDeleteController($uibModalInstance, entity, Bulletin) {
        var vm = this;

        vm.bulletin = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bulletin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
