(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MotifExclusionDeleteController',MotifExclusionDeleteController);

    MotifExclusionDeleteController.$inject = ['$uibModalInstance', 'entity', 'MotifExclusion'];

    function MotifExclusionDeleteController($uibModalInstance, entity, MotifExclusion) {
        var vm = this;

        vm.motifExclusion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MotifExclusion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
