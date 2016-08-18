(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrimestreController', TrimestreController);

    TrimestreController.$inject = ['$scope', '$state', 'Trimestre', 'TrimestreSearch'];

    function TrimestreController ($scope, $state, Trimestre, TrimestreSearch) {
        var vm = this;
        
        vm.trimestres = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Trimestre.query(function(result) {
                vm.trimestres = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TrimestreSearch.query({query: vm.searchQuery}, function(result) {
                vm.trimestres = result;
            });
        }    }
})();
