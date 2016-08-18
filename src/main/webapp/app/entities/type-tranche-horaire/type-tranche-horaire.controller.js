(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeTrancheHoraireController', TypeTrancheHoraireController);

    TypeTrancheHoraireController.$inject = ['$scope', '$state', 'TypeTrancheHoraire', 'TypeTrancheHoraireSearch'];

    function TypeTrancheHoraireController ($scope, $state, TypeTrancheHoraire, TypeTrancheHoraireSearch) {
        var vm = this;
        
        vm.typeTrancheHoraires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeTrancheHoraire.query(function(result) {
                vm.typeTrancheHoraires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeTrancheHoraireSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeTrancheHoraires = result;
            });
        }    }
})();
