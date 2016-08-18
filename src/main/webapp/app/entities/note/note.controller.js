(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NoteController', NoteController);

    NoteController.$inject = ['$scope', '$state', 'Note', 'NoteSearch'];

    function NoteController ($scope, $state, Note, NoteSearch) {
        var vm = this;
        
        vm.notes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Note.query(function(result) {
                vm.notes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.notes = result;
            });
        }    }
})();
