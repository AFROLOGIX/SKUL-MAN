(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeChambreController', TypeChambreController);

    TypeChambreController.$inject = ['$scope', '$state', 'TypeChambre', 'TypeChambreSearch'];

    function TypeChambreController ($scope, $state, TypeChambre, TypeChambreSearch) {
        var vm = this;
        
        vm.typeChambres = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeChambre.query(function(result) {
                vm.typeChambres = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeChambreSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeChambres = result;
            });
        }    }
})();
