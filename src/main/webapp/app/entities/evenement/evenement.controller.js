(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EvenementController', EvenementController);

    EvenementController.$inject = ['$scope', '$state', 'Evenement', 'EvenementSearch'];

    function EvenementController ($scope, $state, Evenement, EvenementSearch) {
        var vm = this;
        
        vm.evenements = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Evenement.query(function(result) {
                vm.evenements = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EvenementSearch.query({query: vm.searchQuery}, function(result) {
                vm.evenements = result;
            });
        }    }
})();
