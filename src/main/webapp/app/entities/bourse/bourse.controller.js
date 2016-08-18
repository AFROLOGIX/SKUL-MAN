(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BourseController', BourseController);

    BourseController.$inject = ['$scope', '$state', 'Bourse', 'BourseSearch'];

    function BourseController ($scope, $state, Bourse, BourseSearch) {
        var vm = this;
        
        vm.bourses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Bourse.query(function(result) {
                vm.bourses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BourseSearch.query({query: vm.searchQuery}, function(result) {
                vm.bourses = result;
            });
        }    }
})();
