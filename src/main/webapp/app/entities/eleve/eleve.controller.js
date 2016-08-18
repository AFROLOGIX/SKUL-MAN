(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EleveController', EleveController);

    EleveController.$inject = ['$scope', '$state', 'Eleve', 'EleveSearch'];

    function EleveController ($scope, $state, Eleve, EleveSearch) {
        var vm = this;
        
        vm.eleves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Eleve.query(function(result) {
                vm.eleves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EleveSearch.query({query: vm.searchQuery}, function(result) {
                vm.eleves = result;
            });
        }    }
})();
