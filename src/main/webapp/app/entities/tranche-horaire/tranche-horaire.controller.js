(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheHoraireController', TrancheHoraireController);

    TrancheHoraireController.$inject = ['$scope', '$state', 'TrancheHoraire', 'TrancheHoraireSearch'];

    function TrancheHoraireController ($scope, $state, TrancheHoraire, TrancheHoraireSearch) {
        var vm = this;
        
        vm.trancheHoraires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TrancheHoraire.query(function(result) {
                vm.trancheHoraires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TrancheHoraireSearch.query({query: vm.searchQuery}, function(result) {
                vm.trancheHoraires = result;
            });
        }    }
})();
