(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FonctionnaliteController', FonctionnaliteController);

    FonctionnaliteController.$inject = ['$scope', '$state', 'Fonctionnalite', 'FonctionnaliteSearch'];

    function FonctionnaliteController ($scope, $state, Fonctionnalite, FonctionnaliteSearch) {
        var vm = this;
        
        vm.fonctionnalites = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Fonctionnalite.query(function(result) {
                vm.fonctionnalites = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FonctionnaliteSearch.query({query: vm.searchQuery}, function(result) {
                vm.fonctionnalites = result;
            });
        }    }
})();
