(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PensionController', PensionController);

    PensionController.$inject = ['$scope', '$state', 'Pension', 'PensionSearch'];

    function PensionController ($scope, $state, Pension, PensionSearch) {
        var vm = this;
        
        vm.pensions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Pension.query(function(result) {
                vm.pensions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PensionSearch.query({query: vm.searchQuery}, function(result) {
                vm.pensions = result;
            });
        }    }
})();
