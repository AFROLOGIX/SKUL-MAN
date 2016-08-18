(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EpreuveController', EpreuveController);

    EpreuveController.$inject = ['$scope', '$state', 'Epreuve', 'EpreuveSearch'];

    function EpreuveController ($scope, $state, Epreuve, EpreuveSearch) {
        var vm = this;
        
        vm.epreuves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Epreuve.query(function(result) {
                vm.epreuves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EpreuveSearch.query({query: vm.searchQuery}, function(result) {
                vm.epreuves = result;
            });
        }    }
})();
