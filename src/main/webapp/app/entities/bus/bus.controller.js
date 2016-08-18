(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BusController', BusController);

    BusController.$inject = ['$scope', '$state', 'Bus', 'BusSearch'];

    function BusController ($scope, $state, Bus, BusSearch) {
        var vm = this;
        
        vm.buses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Bus.query(function(result) {
                vm.buses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BusSearch.query({query: vm.searchQuery}, function(result) {
                vm.buses = result;
            });
        }    }
})();
