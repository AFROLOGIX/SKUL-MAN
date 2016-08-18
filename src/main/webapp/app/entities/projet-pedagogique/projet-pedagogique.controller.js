(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ProjetPedagogiqueController', ProjetPedagogiqueController);

    ProjetPedagogiqueController.$inject = ['$scope', '$state', 'ProjetPedagogique', 'ProjetPedagogiqueSearch'];

    function ProjetPedagogiqueController ($scope, $state, ProjetPedagogique, ProjetPedagogiqueSearch) {
        var vm = this;
        
        vm.projetPedagogiques = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ProjetPedagogique.query(function(result) {
                vm.projetPedagogiques = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProjetPedagogiqueSearch.query({query: vm.searchQuery}, function(result) {
                vm.projetPedagogiques = result;
            });
        }    }
})();
