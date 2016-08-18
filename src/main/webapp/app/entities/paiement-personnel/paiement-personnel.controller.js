(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PaiementPersonnelController', PaiementPersonnelController);

    PaiementPersonnelController.$inject = ['$scope', '$state', 'PaiementPersonnel', 'PaiementPersonnelSearch'];

    function PaiementPersonnelController ($scope, $state, PaiementPersonnel, PaiementPersonnelSearch) {
        var vm = this;
        
        vm.paiementPersonnels = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            PaiementPersonnel.query(function(result) {
                vm.paiementPersonnels = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PaiementPersonnelSearch.query({query: vm.searchQuery}, function(result) {
                vm.paiementPersonnels = result;
            });
        }    }
})();
