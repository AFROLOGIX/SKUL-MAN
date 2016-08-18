(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FichierController', FichierController);

    FichierController.$inject = ['$scope', '$state', 'Fichier', 'FichierSearch'];

    function FichierController ($scope, $state, Fichier, FichierSearch) {
        var vm = this;
        
        vm.fichiers = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Fichier.query(function(result) {
                vm.fichiers = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FichierSearch.query({query: vm.searchQuery}, function(result) {
                vm.fichiers = result;
            });
        }    }
})();
