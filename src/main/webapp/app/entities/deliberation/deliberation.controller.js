(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DeliberationController', DeliberationController);

    DeliberationController.$inject = ['$scope', '$state', 'Deliberation', 'DeliberationSearch'];

    function DeliberationController ($scope, $state, Deliberation, DeliberationSearch) {
        var vm = this;
        
        vm.deliberations = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Deliberation.query(function(result) {
                vm.deliberations = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DeliberationSearch.query({query: vm.searchQuery}, function(result) {
                vm.deliberations = result;
            });
        }    }
})();
