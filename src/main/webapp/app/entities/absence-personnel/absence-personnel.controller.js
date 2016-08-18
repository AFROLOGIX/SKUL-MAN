(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsencePersonnelController', AbsencePersonnelController);

    AbsencePersonnelController.$inject = ['$scope', '$state', 'AbsencePersonnel', 'AbsencePersonnelSearch'];

    function AbsencePersonnelController ($scope, $state, AbsencePersonnel, AbsencePersonnelSearch) {
        var vm = this;
        
        vm.absencePersonnels = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AbsencePersonnel.query(function(result) {
                vm.absencePersonnels = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AbsencePersonnelSearch.query({query: vm.searchQuery}, function(result) {
                vm.absencePersonnels = result;
            });
        }    }
})();
