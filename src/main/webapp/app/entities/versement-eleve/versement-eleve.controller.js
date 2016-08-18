(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('VersementEleveController', VersementEleveController);

    VersementEleveController.$inject = ['$scope', '$state', 'VersementEleve', 'VersementEleveSearch'];

    function VersementEleveController ($scope, $state, VersementEleve, VersementEleveSearch) {
        var vm = this;
        
        vm.versementEleves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            VersementEleve.query(function(result) {
                vm.versementEleves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            VersementEleveSearch.query({query: vm.searchQuery}, function(result) {
                vm.versementEleves = result;
            });
        }    }
})();
