(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeChambreDetailController', TypeChambreDetailController);

    TypeChambreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeChambre'];

    function TypeChambreDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeChambre) {
        var vm = this;

        vm.typeChambre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typeChambreUpdate', function(event, result) {
            vm.typeChambre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
