(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SerieController', SerieController);

    SerieController.$inject = ['$scope', '$state', 'Serie', 'SerieSearch'];

    function SerieController ($scope, $state, Serie, SerieSearch) {
        var vm = this;
        
        vm.series = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Serie.query(function(result) {
                vm.series = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SerieSearch.query({query: vm.searchQuery}, function(result) {
                vm.series = result;
            });
        }    }
})();
