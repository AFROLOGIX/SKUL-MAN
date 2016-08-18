(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BatimentController', BatimentController);

    BatimentController.$inject = ['$scope', '$state', 'Batiment', 'BatimentSearch'];

    function BatimentController ($scope, $state, Batiment, BatimentSearch) {
        var vm = this;
        
        vm.batiments = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Batiment.query(function(result) {
                vm.batiments = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BatimentSearch.query({query: vm.searchQuery}, function(result) {
                vm.batiments = result;
            });
        }    }
})();
