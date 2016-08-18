(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AgentAdministratifDetailController', AgentAdministratifDetailController);

    AgentAdministratifDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AgentAdministratif', 'Salaire', 'Fichier', 'Deliberation'];

    function AgentAdministratifDetailController($scope, $rootScope, $stateParams, previousState, entity, AgentAdministratif, Salaire, Fichier, Deliberation) {
        var vm = this;

        vm.agentAdministratif = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:agentAdministratifUpdate', function(event, result) {
            vm.agentAdministratif = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
