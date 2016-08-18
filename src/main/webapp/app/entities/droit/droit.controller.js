(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DroitController', DroitController);

    DroitController.$inject = ['$scope', '$state', 'Droit', 'DroitSearch'];

    function DroitController ($scope, $state, Droit, DroitSearch) {
        var vm = this;
        
        vm.droits = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Droit.query(function(result) {
                vm.droits = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DroitSearch.query({query: vm.searchQuery}, function(result) {
                vm.droits = result;
            });
        }    }
})();
