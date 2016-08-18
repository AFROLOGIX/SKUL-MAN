(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheController', TrancheController);

    TrancheController.$inject = ['$scope', '$state', 'Tranche', 'TrancheSearch'];

    function TrancheController ($scope, $state, Tranche, TrancheSearch) {
        var vm = this;
        
        vm.tranches = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Tranche.query(function(result) {
                vm.tranches = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TrancheSearch.query({query: vm.searchQuery}, function(result) {
                vm.tranches = result;
            });
        }    }
})();
