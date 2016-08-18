(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PersonnelDeleteController',PersonnelDeleteController);

    PersonnelDeleteController.$inject = ['$uibModalInstance', 'entity', 'Personnel'];

    function PersonnelDeleteController($uibModalInstance, entity, Personnel) {
        var vm = this;

        vm.personnel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Personnel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
