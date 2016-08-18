(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantController', EnseignantController);

    EnseignantController.$inject = ['$scope', '$state', 'Enseignant', 'EnseignantSearch'];

    function EnseignantController ($scope, $state, Enseignant, EnseignantSearch) {
        var vm = this;
        
        vm.enseignants = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Enseignant.query(function(result) {
                vm.enseignants = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnseignantSearch.query({query: vm.searchQuery}, function(result) {
                vm.enseignants = result;
            });
        }    }
})();
