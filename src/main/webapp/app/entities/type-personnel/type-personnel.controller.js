(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypePersonnelController', TypePersonnelController);

    TypePersonnelController.$inject = ['$scope', '$state', 'TypePersonnel', 'TypePersonnelSearch'];

    function TypePersonnelController ($scope, $state, TypePersonnel, TypePersonnelSearch) {
        var vm = this;
        
        vm.typePersonnels = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypePersonnel.query(function(result) {
                vm.typePersonnels = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypePersonnelSearch.query({query: vm.searchQuery}, function(result) {
                vm.typePersonnels = result;
            });
        }    }
})();
