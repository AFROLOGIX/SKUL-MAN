(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeEpreuveController', TypeEpreuveController);

    TypeEpreuveController.$inject = ['$scope', '$state', 'TypeEpreuve', 'TypeEpreuveSearch'];

    function TypeEpreuveController ($scope, $state, TypeEpreuve, TypeEpreuveSearch) {
        var vm = this;
        
        vm.typeEpreuves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeEpreuve.query(function(result) {
                vm.typeEpreuves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeEpreuveSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeEpreuves = result;
            });
        }    }
})();
