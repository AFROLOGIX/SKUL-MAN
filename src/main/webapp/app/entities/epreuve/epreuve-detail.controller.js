(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EpreuveDetailController', EpreuveDetailController);

    EpreuveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Epreuve', 'TypeEpreuve', 'Cours'];

    function EpreuveDetailController($scope, $rootScope, $stateParams, previousState, entity, Epreuve, TypeEpreuve, Cours) {
        var vm = this;

        vm.epreuve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:epreuveUpdate', function(event, result) {
            vm.epreuve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
