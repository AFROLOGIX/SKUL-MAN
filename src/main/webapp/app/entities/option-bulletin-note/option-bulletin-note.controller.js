(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('OptionBulletinNoteController', OptionBulletinNoteController);

    OptionBulletinNoteController.$inject = ['$scope', '$state', 'OptionBulletinNote', 'OptionBulletinNoteSearch'];

    function OptionBulletinNoteController ($scope, $state, OptionBulletinNote, OptionBulletinNoteSearch) {
        var vm = this;
        
        vm.optionBulletinNotes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            OptionBulletinNote.query(function(result) {
                vm.optionBulletinNotes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OptionBulletinNoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.optionBulletinNotes = result;
            });
        }    }
})();
