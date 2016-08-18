(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ReligionController', ReligionController);

    ReligionController.$inject = ['$scope', '$state', 'Religion', 'ReligionSearch'];

    function ReligionController ($scope, $state, Religion, ReligionSearch) {
        var vm = this;
        
        vm.religions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Religion.query(function(result) {
                vm.religions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ReligionSearch.query({query: vm.searchQuery}, function(result) {
                vm.religions = result;
            });
        }    }
})();
