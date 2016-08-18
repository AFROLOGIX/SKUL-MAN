(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoratoireController', MoratoireController);

    MoratoireController.$inject = ['$scope', '$state', 'Moratoire', 'MoratoireSearch'];

    function MoratoireController ($scope, $state, Moratoire, MoratoireSearch) {
        var vm = this;
        
        vm.moratoires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Moratoire.query(function(result) {
                vm.moratoires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MoratoireSearch.query({query: vm.searchQuery}, function(result) {
                vm.moratoires = result;
            });
        }    }
})();
