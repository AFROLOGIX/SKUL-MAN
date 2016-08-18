(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreController', ChambreController);

    ChambreController.$inject = ['$scope', '$state', 'Chambre', 'ChambreSearch'];

    function ChambreController ($scope, $state, Chambre, ChambreSearch) {
        var vm = this;
        
        vm.chambres = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Chambre.query(function(result) {
                vm.chambres = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ChambreSearch.query({query: vm.searchQuery}, function(result) {
                vm.chambres = result;
            });
        }    }
})();
