(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereClasseController', MatiereClasseController);

    MatiereClasseController.$inject = ['$scope', '$state', 'MatiereClasse', 'MatiereClasseSearch'];

    function MatiereClasseController ($scope, $state, MatiereClasse, MatiereClasseSearch) {
        var vm = this;
        
        vm.matiereClasses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MatiereClasse.query(function(result) {
                vm.matiereClasses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MatiereClasseSearch.query({query: vm.searchQuery}, function(result) {
                vm.matiereClasses = result;
            });
        }    }
})();
