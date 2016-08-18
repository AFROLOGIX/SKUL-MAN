(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParentController', ParentController);

    ParentController.$inject = ['$scope', '$state', 'Parent', 'ParentSearch'];

    function ParentController ($scope, $state, Parent, ParentSearch) {
        var vm = this;
        
        vm.parents = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Parent.query(function(result) {
                vm.parents = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ParentSearch.query({query: vm.searchQuery}, function(result) {
                vm.parents = result;
            });
        }    }
})();
