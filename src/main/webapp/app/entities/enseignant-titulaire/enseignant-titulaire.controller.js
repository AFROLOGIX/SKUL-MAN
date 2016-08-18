(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantTitulaireController', EnseignantTitulaireController);

    EnseignantTitulaireController.$inject = ['$scope', '$state', 'EnseignantTitulaire', 'EnseignantTitulaireSearch'];

    function EnseignantTitulaireController ($scope, $state, EnseignantTitulaire, EnseignantTitulaireSearch) {
        var vm = this;
        
        vm.enseignantTitulaires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EnseignantTitulaire.query(function(result) {
                vm.enseignantTitulaires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnseignantTitulaireSearch.query({query: vm.searchQuery}, function(result) {
                vm.enseignantTitulaires = result;
            });
        }    }
})();
