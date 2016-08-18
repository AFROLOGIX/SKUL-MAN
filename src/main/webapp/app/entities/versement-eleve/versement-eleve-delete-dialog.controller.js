(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('VersementEleveDeleteController',VersementEleveDeleteController);

    VersementEleveDeleteController.$inject = ['$uibModalInstance', 'entity', 'VersementEleve'];

    function VersementEleveDeleteController($uibModalInstance, entity, VersementEleve) {
        var vm = this;

        vm.versementEleve = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VersementEleve.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
