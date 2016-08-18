(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('LoginConnexionDetailController', LoginConnexionDetailController);

    LoginConnexionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LoginConnexion', 'Utilisateur'];

    function LoginConnexionDetailController($scope, $rootScope, $stateParams, previousState, entity, LoginConnexion, Utilisateur) {
        var vm = this;

        vm.loginConnexion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:loginConnexionUpdate', function(event, result) {
            vm.loginConnexion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
