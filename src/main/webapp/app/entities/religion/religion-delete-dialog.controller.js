(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ReligionDeleteController',ReligionDeleteController);

    ReligionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Religion'];

    function ReligionDeleteController($uibModalInstance, entity, Religion) {
        var vm = this;

        vm.religion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Religion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
