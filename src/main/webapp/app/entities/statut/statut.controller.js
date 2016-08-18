(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutController', StatutController);

    StatutController.$inject = ['$scope', '$state', 'Statut', 'StatutSearch'];

    function StatutController ($scope, $state, Statut, StatutSearch) {
        var vm = this;
        
        vm.statuts = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Statut.query(function(result) {
                vm.statuts = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StatutSearch.query({query: vm.searchQuery}, function(result) {
                vm.statuts = result;
            });
        }    }
})();
