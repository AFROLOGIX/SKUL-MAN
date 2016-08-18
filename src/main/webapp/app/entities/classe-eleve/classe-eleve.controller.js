(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseEleveController', ClasseEleveController);

    ClasseEleveController.$inject = ['$scope', '$state', 'ClasseEleve', 'ClasseEleveSearch'];

    function ClasseEleveController ($scope, $state, ClasseEleve, ClasseEleveSearch) {
        var vm = this;
        
        vm.classeEleves = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ClasseEleve.query(function(result) {
                vm.classeEleves = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClasseEleveSearch.query({query: vm.searchQuery}, function(result) {
                vm.classeEleves = result;
            });
        }    }
})();
