(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeAbonnementBusController', TypeAbonnementBusController);

    TypeAbonnementBusController.$inject = ['$scope', '$state', 'TypeAbonnementBus', 'TypeAbonnementBusSearch'];

    function TypeAbonnementBusController ($scope, $state, TypeAbonnementBus, TypeAbonnementBusSearch) {
        var vm = this;
        
        vm.typeAbonnementBuses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeAbonnementBus.query(function(result) {
                vm.typeAbonnementBuses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeAbonnementBusSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeAbonnementBuses = result;
            });
        }    }
})();
