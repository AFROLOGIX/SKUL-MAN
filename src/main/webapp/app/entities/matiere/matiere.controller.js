(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereController', MatiereController);

    MatiereController.$inject = ['$scope', '$state', 'Matiere', 'MatiereSearch'];

    function MatiereController ($scope, $state, Matiere, MatiereSearch) {
        var vm = this;
        
        vm.matieres = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Matiere.query(function(result) {
                vm.matieres = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MatiereSearch.query({query: vm.searchQuery}, function(result) {
                vm.matieres = result;
            });
        }    }
})();
