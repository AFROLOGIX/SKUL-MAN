(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('GroupeUtilisateurController', GroupeUtilisateurController);

    GroupeUtilisateurController.$inject = ['$scope', '$state', 'GroupeUtilisateur', 'GroupeUtilisateurSearch'];

    function GroupeUtilisateurController ($scope, $state, GroupeUtilisateur, GroupeUtilisateurSearch) {
        var vm = this;
        
        vm.groupeUtilisateurs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            GroupeUtilisateur.query(function(result) {
                vm.groupeUtilisateurs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GroupeUtilisateurSearch.query({query: vm.searchQuery}, function(result) {
                vm.groupeUtilisateurs = result;
            });
        }    }
})();
