(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NiveauController', NiveauController);

    NiveauController.$inject = ['$scope', '$state', 'Niveau', 'NiveauSearch'];

    function NiveauController ($scope, $state, Niveau, NiveauSearch) {
        var vm = this;
        
        vm.niveaus = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Niveau.query(function(result) {
                vm.niveaus = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NiveauSearch.query({query: vm.searchQuery}, function(result) {
                vm.niveaus = result;
            });
        }    }
})();
