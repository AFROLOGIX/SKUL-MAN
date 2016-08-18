(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginActionDetailController', LoginActionDetailController);

    LoginActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LoginAction', 'Utilisateur'];

    function LoginActionDetailController($scope, $rootScope, $stateParams, previousState, entity, LoginAction, Utilisateur) {
        var vm = this;

        vm.loginAction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:loginActionUpdate', function(event, result) {
            vm.loginAction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
