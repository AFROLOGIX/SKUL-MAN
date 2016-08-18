(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SerieDeleteController',SerieDeleteController);

    SerieDeleteController.$inject = ['$uibModalInstance', 'entity', 'Serie'];

    function SerieDeleteController($uibModalInstance, entity, Serie) {
        var vm = this;

        vm.serie = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Serie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
