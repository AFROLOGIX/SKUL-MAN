(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FormatMatriculeDeleteController',FormatMatriculeDeleteController);

    FormatMatriculeDeleteController.$inject = ['$uibModalInstance', 'entity', 'FormatMatricule'];

    function FormatMatriculeDeleteController($uibModalInstance, entity, FormatMatricule) {
        var vm = this;

        vm.formatMatricule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FormatMatricule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
