(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalaireController', SalaireController);

    SalaireController.$inject = ['$scope', '$state', 'Salaire', 'SalaireSearch'];

    function SalaireController ($scope, $state, Salaire, SalaireSearch) {
        var vm = this;
        
        vm.salaires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Salaire.query(function(result) {
                vm.salaires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SalaireSearch.query({query: vm.searchQuery}, function(result) {
                vm.salaires = result;
            });
        }    }
})();
