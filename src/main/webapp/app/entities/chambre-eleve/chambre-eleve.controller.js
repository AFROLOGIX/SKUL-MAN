(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreEleveController', ChambreEleveController);

    ChambreEleveController.$inject = ['$scope', '$state', 'ChambreEleve', 'ChambreEleveSearch'];

    function ChambreEleveController ($scope, $state, ChambreEleve, ChambreEleveSearch) {
        var vm = this;
        
        vm.chambreEleves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ChambreEleve.query(function(result) {
                vm.chambreEleves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ChambreEleveSearch.query({query: vm.searchQuery}, function(result) {
                vm.chambreEleves = result;
            });
        }    }
})();
