(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('VacationController', VacationController);

    VacationController.$inject = ['$scope', '$state', 'Vacation', 'VacationSearch'];

    function VacationController ($scope, $state, Vacation, VacationSearch) {
        var vm = this;
        
        vm.vacations = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Vacation.query(function(result) {
                vm.vacations = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            VacationSearch.query({query: vm.searchQuery}, function(result) {
                vm.vacations = result;
            });
        }    }
})();
