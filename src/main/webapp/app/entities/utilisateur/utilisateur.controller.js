(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('UtilisateurController', UtilisateurController);

    UtilisateurController.$inject = ['$scope', '$state', 'Utilisateur', 'UtilisateurSearch'];

    function UtilisateurController ($scope, $state, Utilisateur, UtilisateurSearch) {
        var vm = this;
        
        vm.utilisateurs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Utilisateur.query(function(result) {
                vm.utilisateurs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            UtilisateurSearch.query({query: vm.searchQuery}, function(result) {
                vm.utilisateurs = result;
            });
        }    }
})();
