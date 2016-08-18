(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CycleController', CycleController);

    CycleController.$inject = ['$scope', '$state', 'Cycle', 'CycleSearch'];

    function CycleController ($scope, $state, Cycle, CycleSearch) {
        var vm = this;
        
        vm.cycles = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Cycle.query(function(result) {
                vm.cycles = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CycleSearch.query({query: vm.searchQuery}, function(result) {
                vm.cycles = result;
            });
        }    }
})();
