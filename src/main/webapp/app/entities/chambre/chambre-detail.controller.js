(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreDetailController', ChambreDetailController);

    ChambreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chambre', 'Batiment', 'TypeChambre'];

    function ChambreDetailController($scope, $rootScope, $stateParams, previousState, entity, Chambre, Batiment, TypeChambre) {
        var vm = this;

        vm.chambre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:chambreUpdate', function(event, result) {
            vm.chambre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
