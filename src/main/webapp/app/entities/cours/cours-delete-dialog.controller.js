(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CoursDeleteController',CoursDeleteController);

    CoursDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cours'];

    function CoursDeleteController($uibModalInstance, entity, Cours) {
        var vm = this;

        vm.cours = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
