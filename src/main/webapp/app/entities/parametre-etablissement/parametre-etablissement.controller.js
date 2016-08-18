(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParametreEtablissementController', ParametreEtablissementController);

    ParametreEtablissementController.$inject = ['$scope', '$state', 'ParametreEtablissement', 'ParametreEtablissementSearch'];

    function ParametreEtablissementController ($scope, $state, ParametreEtablissement, ParametreEtablissementSearch) {
        var vm = this;
        
        vm.parametreEtablissements = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ParametreEtablissement.query(function(result) {
                vm.parametreEtablissements = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ParametreEtablissementSearch.query({query: vm.searchQuery}, function(result) {
                vm.parametreEtablissements = result;
            });
        }    }
})();
