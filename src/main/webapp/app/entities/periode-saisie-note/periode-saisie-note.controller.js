(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PeriodeSaisieNoteController', PeriodeSaisieNoteController);

    PeriodeSaisieNoteController.$inject = ['$scope', '$state', 'PeriodeSaisieNote', 'PeriodeSaisieNoteSearch'];

    function PeriodeSaisieNoteController ($scope, $state, PeriodeSaisieNote, PeriodeSaisieNoteSearch) {
        var vm = this;
        
        vm.periodeSaisieNotes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            PeriodeSaisieNote.query(function(result) {
                vm.periodeSaisieNotes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PeriodeSaisieNoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.periodeSaisieNotes = result;
            });
        }    }
})();
