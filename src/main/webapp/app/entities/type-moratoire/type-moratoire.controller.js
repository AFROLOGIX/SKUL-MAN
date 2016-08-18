(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeMoratoireController', TypeMoratoireController);

    TypeMoratoireController.$inject = ['$scope', '$state', 'TypeMoratoire', 'TypeMoratoireSearch'];

    function TypeMoratoireController ($scope, $state, TypeMoratoire, TypeMoratoireSearch) {
        var vm = this;
        
        vm.typeMoratoires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TypeMoratoire.query(function(result) {
                vm.typeMoratoires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeMoratoireSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeMoratoires = result;
            });
        }    }
})();
