(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SectionController', SectionController);

    SectionController.$inject = ['$scope', '$state', 'Section', 'SectionSearch'];

    function SectionController ($scope, $state, Section, SectionSearch) {
        var vm = this;
        
        vm.sections = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Section.query(function(result) {
                vm.sections = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SectionSearch.query({query: vm.searchQuery}, function(result) {
                vm.sections = result;
            });
        }    }
})();
