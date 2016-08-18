(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AnneeScolaireController', AnneeScolaireController);

    AnneeScolaireController.$inject = ['$scope', '$state', 'AnneeScolaire', 'AnneeScolaireSearch'];

    function AnneeScolaireController ($scope, $state, AnneeScolaire, AnneeScolaireSearch) {
        var vm = this;
        
        vm.anneeScolaires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AnneeScolaire.query(function(result) {
                vm.anneeScolaires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AnneeScolaireSearch.query({query: vm.searchQuery}, function(result) {
                vm.anneeScolaires = result;
            });
        }    }
})();
