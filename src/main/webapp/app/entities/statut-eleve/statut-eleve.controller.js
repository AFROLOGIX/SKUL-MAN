(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutEleveController', StatutEleveController);

    StatutEleveController.$inject = ['$scope', '$state', 'StatutEleve', 'StatutEleveSearch'];

    function StatutEleveController ($scope, $state, StatutEleve, StatutEleveSearch) {
        var vm = this;
        
        vm.statutEleves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            StatutEleve.query(function(result) {
                vm.statutEleves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StatutEleveSearch.query({query: vm.searchQuery}, function(result) {
                vm.statutEleves = result;
            });
        }    }
})();
