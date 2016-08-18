(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseController', ClasseController);

    ClasseController.$inject = ['$scope', '$state', 'Classe', 'ClasseSearch'];

    function ClasseController ($scope, $state, Classe, ClasseSearch) {
        var vm = this;
        
        vm.classes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Classe.query(function(result) {
                vm.classes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClasseSearch.query({query: vm.searchQuery}, function(result) {
                vm.classes = result;
            });
        }    }
})();
