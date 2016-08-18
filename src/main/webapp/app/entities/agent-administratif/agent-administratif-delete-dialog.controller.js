(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AgentAdministratifDeleteController',AgentAdministratifDeleteController);

    AgentAdministratifDeleteController.$inject = ['$uibModalInstance', 'entity', 'AgentAdministratif'];

    function AgentAdministratifDeleteController($uibModalInstance, entity, AgentAdministratif) {
        var vm = this;

        vm.agentAdministratif = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AgentAdministratif.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
