(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('DeliberationDetailController', DeliberationDetailController);

    DeliberationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Deliberation', 'Eleve', 'AgentAdministratif', 'Enseignant'];

    function DeliberationDetailController($scope, $rootScope, $stateParams, previousState, entity, Deliberation, Eleve, AgentAdministratif, Enseignant) {
        var vm = this;

        vm.deliberation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:deliberationUpdate', function(event, result) {
            vm.deliberation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
