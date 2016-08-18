(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PersonnelController', PersonnelController);

    PersonnelController.$inject = ['$scope', '$state', 'Personnel', 'PersonnelSearch'];

    function PersonnelController ($scope, $state, Personnel, PersonnelSearch) {
        var vm = this;
        
        vm.personnels = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Personnel.query(function(result) {
                vm.personnels = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PersonnelSearch.query({query: vm.searchQuery}, function(result) {
                vm.personnels = result;
            });
        }    }
})();
