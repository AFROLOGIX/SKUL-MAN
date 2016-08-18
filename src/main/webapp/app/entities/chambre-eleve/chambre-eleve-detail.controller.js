(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreEleveDetailController', ChambreEleveDetailController);

    ChambreEleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChambreEleve', 'Chambre'];

    function ChambreEleveDetailController($scope, $rootScope, $stateParams, previousState, entity, ChambreEleve, Chambre) {
        var vm = this;

        vm.chambreEleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:chambreEleveUpdate', function(event, result) {
            vm.chambreEleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
