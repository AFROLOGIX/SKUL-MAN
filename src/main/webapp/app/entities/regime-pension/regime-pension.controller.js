(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('RegimePensionController', RegimePensionController);

    RegimePensionController.$inject = ['$scope', '$state', 'RegimePension', 'RegimePensionSearch'];

    function RegimePensionController ($scope, $state, RegimePension, RegimePensionSearch) {
        var vm = this;
        
        vm.regimePensions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            RegimePension.query(function(result) {
                vm.regimePensions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RegimePensionSearch.query({query: vm.searchQuery}, function(result) {
                vm.regimePensions = result;
            });
        }    }
})();
