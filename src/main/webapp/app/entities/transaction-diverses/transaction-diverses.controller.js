(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TransactionDiversesController', TransactionDiversesController);

    TransactionDiversesController.$inject = ['$scope', '$state', 'TransactionDiverses', 'TransactionDiversesSearch'];

    function TransactionDiversesController ($scope, $state, TransactionDiverses, TransactionDiversesSearch) {
        var vm = this;
        
        vm.transactionDiverses = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TransactionDiverses.query(function(result) {
                vm.transactionDiverses = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TransactionDiversesSearch.query({query: vm.searchQuery}, function(result) {
                vm.transactionDiverses = result;
            });
        }    }
})();
