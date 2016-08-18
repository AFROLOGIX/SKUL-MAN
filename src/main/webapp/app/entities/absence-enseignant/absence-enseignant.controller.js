(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEnseignantController', AbsenceEnseignantController);

    AbsenceEnseignantController.$inject = ['$scope', '$state', 'AbsenceEnseignant', 'AbsenceEnseignantSearch'];

    function AbsenceEnseignantController ($scope, $state, AbsenceEnseignant, AbsenceEnseignantSearch) {
        var vm = this;
        
        vm.absenceEnseignants = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AbsenceEnseignant.query(function(result) {
                vm.absenceEnseignants = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AbsenceEnseignantSearch.query({query: vm.searchQuery}, function(result) {
                vm.absenceEnseignants = result;
            });
        }    }
})();
