(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EtablissementController', EtablissementController);

    EtablissementController.$inject = ['$scope', '$state', 'Etablissement', 'EtablissementSearch'];

    function EtablissementController ($scope, $state, Etablissement, EtablissementSearch) {
        var vm = this;
        
        vm.etablissements = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Etablissement.query(function(result) {
                vm.etablissements = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EtablissementSearch.query({query: vm.searchQuery}, function(result) {
                vm.etablissements = result;
            });
        }    }
})();
