(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PaiementPersonnelDetailController', PaiementPersonnelDetailController);

    PaiementPersonnelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaiementPersonnel', 'AgentAdministratif', 'Operation', 'Personnel'];

    function PaiementPersonnelDetailController($scope, $rootScope, $stateParams, previousState, entity, PaiementPersonnel, AgentAdministratif, Operation, Personnel) {
        var vm = this;

        vm.paiementPersonnel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:paiementPersonnelUpdate', function(event, result) {
            vm.paiementPersonnel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
