(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CoursController', CoursController);

    CoursController.$inject = ['$scope', '$state', 'Cours', 'CoursSearch'];

    function CoursController ($scope, $state, Cours, CoursSearch) {
        var vm = this;
        
        vm.cours = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Cours.query(function(result) {
                vm.cours = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoursSearch.query({query: vm.searchQuery}, function(result) {
                vm.cours = result;
            });
        }    }
})();
