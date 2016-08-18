(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TarifBusController', TarifBusController);

    TarifBusController.$inject = ['$scope', '$state', 'TarifBus', 'TarifBusSearch'];

    function TarifBusController ($scope, $state, TarifBus, TarifBusSearch) {
        var vm = this;
        
        vm.tarifBuses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TarifBus.query(function(result) {
                vm.tarifBuses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TarifBusSearch.query({query: vm.searchQuery}, function(result) {
                vm.tarifBuses = result;
            });
        }    }
})();
