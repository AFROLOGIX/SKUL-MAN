(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('JourController', JourController);

    JourController.$inject = ['$scope', '$state', 'Jour', 'JourSearch'];

    function JourController ($scope, $state, Jour, JourSearch) {
        var vm = this;
        
        vm.jours = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Jour.query(function(result) {
                vm.jours = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            JourSearch.query({query: vm.searchQuery}, function(result) {
                vm.jours = result;
            });
        }    }
})();
