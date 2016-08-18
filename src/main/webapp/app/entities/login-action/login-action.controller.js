(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginActionController', LoginActionController);

    LoginActionController.$inject = ['$scope', '$state', 'LoginAction', 'LoginActionSearch'];

    function LoginActionController ($scope, $state, LoginAction, LoginActionSearch) {
        var vm = this;
        
        vm.loginActions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LoginAction.query(function(result) {
                vm.loginActions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LoginActionSearch.query({query: vm.searchQuery}, function(result) {
                vm.loginActions = result;
            });
        }    }
})();
