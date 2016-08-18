(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BourseDeleteController',BourseDeleteController);

    BourseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bourse'];

    function BourseDeleteController($uibModalInstance, entity, Bourse) {
        var vm = this;

        vm.bourse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bourse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
