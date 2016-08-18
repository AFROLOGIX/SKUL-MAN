(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoyenneTableauHonneurController', MoyenneTableauHonneurController);

    MoyenneTableauHonneurController.$inject = ['$scope', '$state', 'MoyenneTableauHonneur', 'MoyenneTableauHonneurSearch'];

    function MoyenneTableauHonneurController ($scope, $state, MoyenneTableauHonneur, MoyenneTableauHonneurSearch) {
        var vm = this;
        
        vm.moyenneTableauHonneurs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MoyenneTableauHonneur.query(function(result) {
                vm.moyenneTableauHonneurs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MoyenneTableauHonneurSearch.query({query: vm.searchQuery}, function(result) {
                vm.moyenneTableauHonneurs = result;
            });
        }    }
})();
