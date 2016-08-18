(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginConnexionController', LoginConnexionController);

    LoginConnexionController.$inject = ['$scope', '$state', 'LoginConnexion', 'LoginConnexionSearch'];

    function LoginConnexionController ($scope, $state, LoginConnexion, LoginConnexionSearch) {
        var vm = this;
        
        vm.loginConnexions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LoginConnexion.query(function(result) {
                vm.loginConnexions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LoginConnexionSearch.query({query: vm.searchQuery}, function(result) {
                vm.loginConnexions = result;
            });
        }    }
})();
