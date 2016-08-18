(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MotifExclusionController', MotifExclusionController);

    MotifExclusionController.$inject = ['$scope', '$state', 'MotifExclusion', 'MotifExclusionSearch'];

    function MotifExclusionController ($scope, $state, MotifExclusion, MotifExclusionSearch) {
        var vm = this;
        
        vm.motifExclusions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MotifExclusion.query(function(result) {
                vm.motifExclusions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MotifExclusionSearch.query({query: vm.searchQuery}, function(result) {
                vm.motifExclusions = result;
            });
        }    }
})();
