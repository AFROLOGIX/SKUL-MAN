(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeOperationController', TypeOperationController);

    TypeOperationController.$inject = ['$scope', '$state', 'TypeOperation', 'TypeOperationSearch'];

    function TypeOperationController ($scope, $state, TypeOperation, TypeOperationSearch) {
        var vm = this;
        
        vm.typeOperations = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeOperation.query(function(result) {
                vm.typeOperations = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeOperationSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeOperations = result;
            });
        }    }
})();
