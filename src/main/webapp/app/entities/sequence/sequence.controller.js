(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SequenceController', SequenceController);

    SequenceController.$inject = ['$scope', '$state', 'Sequence', 'SequenceSearch'];

    function SequenceController ($scope, $state, Sequence, SequenceSearch) {
        var vm = this;
        
        vm.sequences = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Sequence.query(function(result) {
                vm.sequences = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SequenceSearch.query({query: vm.searchQuery}, function(result) {
                vm.sequences = result;
            });
        }    }
})();
