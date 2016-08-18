(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CompteController', CompteController);

    CompteController.$inject = ['$scope', '$state', 'Compte', 'CompteSearch'];

    function CompteController ($scope, $state, Compte, CompteSearch) {
        var vm = this;
        
        vm.comptes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Compte.query(function(result) {
                vm.comptes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompteSearch.query({query: vm.searchQuery}, function(result) {
                vm.comptes = result;
            });
        }    }
})();
