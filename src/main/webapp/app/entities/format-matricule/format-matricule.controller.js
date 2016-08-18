(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FormatMatriculeController', FormatMatriculeController);

    FormatMatriculeController.$inject = ['$scope', '$state', 'FormatMatricule', 'FormatMatriculeSearch'];

    function FormatMatriculeController ($scope, $state, FormatMatricule, FormatMatriculeSearch) {
        var vm = this;
        
        vm.formatMatricules = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FormatMatricule.query(function(result) {
                vm.formatMatricules = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormatMatriculeSearch.query({query: vm.searchQuery}, function(result) {
                vm.formatMatricules = result;
            });
        }    }
})();
