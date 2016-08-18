(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AgentAdministratifController', AgentAdministratifController);

    AgentAdministratifController.$inject = ['$scope', '$state', 'AgentAdministratif', 'AgentAdministratifSearch'];

    function AgentAdministratifController ($scope, $state, AgentAdministratif, AgentAdministratifSearch) {
        var vm = this;
        
        vm.agentAdministratifs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AgentAdministratif.query(function(result) {
                vm.agentAdministratifs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AgentAdministratifSearch.query({query: vm.searchQuery}, function(result) {
                vm.agentAdministratifs = result;
            });
        }    }
})();
