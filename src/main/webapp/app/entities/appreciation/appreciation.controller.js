(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AppreciationController', AppreciationController);

    AppreciationController.$inject = ['$scope', '$state', 'Appreciation', 'AppreciationSearch'];

    function AppreciationController ($scope, $state, Appreciation, AppreciationSearch) {
        var vm = this;
        
        vm.appreciations = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Appreciation.query(function(result) {
                vm.appreciations = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AppreciationSearch.query({query: vm.searchQuery}, function(result) {
                vm.appreciations = result;
            });
        }    }
})();
