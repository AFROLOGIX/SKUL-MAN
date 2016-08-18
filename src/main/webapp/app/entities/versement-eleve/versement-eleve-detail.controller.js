(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('VersementEleveDetailController', VersementEleveDetailController);

    VersementEleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VersementEleve', 'AgentAdministratif', 'Eleve', 'Operation'];

    function VersementEleveDetailController($scope, $rootScope, $stateParams, previousState, entity, VersementEleve, AgentAdministratif, Eleve, Operation) {
        var vm = this;

        vm.versementEleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:versementEleveUpdate', function(event, result) {
            vm.versementEleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
