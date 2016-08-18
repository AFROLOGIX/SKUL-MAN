(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PauseController', PauseController);

    PauseController.$inject = ['$scope', '$state', 'Pause', 'PauseSearch'];

    function PauseController ($scope, $state, Pause, PauseSearch) {
        var vm = this;
        
        vm.pauses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Pause.query(function(result) {
                vm.pauses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PauseSearch.query({query: vm.searchQuery}, function(result) {
                vm.pauses = result;
            });
        }    }
})();
