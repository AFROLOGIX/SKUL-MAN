(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NiveauDetailController', NiveauDetailController);

    NiveauDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Niveau', 'Classe', 'Cycle'];

    function NiveauDetailController($scope, $rootScope, $stateParams, previousState, entity, Niveau, Classe, Cycle) {
        var vm = this;

        vm.niveau = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:niveauUpdate', function(event, result) {
            vm.niveau = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
