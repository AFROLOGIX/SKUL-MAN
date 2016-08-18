(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalleController', SalleController);

    SalleController.$inject = ['$scope', '$state', 'Salle', 'SalleSearch'];

    function SalleController ($scope, $state, Salle, SalleSearch) {
        var vm = this;
        
        vm.salles = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Salle.query(function(result) {
                vm.salles = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SalleSearch.query({query: vm.searchQuery}, function(result) {
                vm.salles = result;
            });
        }    }
})();
