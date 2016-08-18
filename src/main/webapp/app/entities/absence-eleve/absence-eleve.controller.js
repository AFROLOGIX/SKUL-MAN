(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEleveController', AbsenceEleveController);

    AbsenceEleveController.$inject = ['$scope', '$state', 'AbsenceEleve', 'AbsenceEleveSearch'];

    function AbsenceEleveController ($scope, $state, AbsenceEleve, AbsenceEleveSearch) {
        var vm = this;
        
        vm.absenceEleves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AbsenceEleve.query(function(result) {
                vm.absenceEleves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AbsenceEleveSearch.query({query: vm.searchQuery}, function(result) {
                vm.absenceEleves = result;
            });
        }    }
})();
