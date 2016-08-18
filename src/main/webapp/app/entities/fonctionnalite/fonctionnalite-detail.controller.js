(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FonctionnaliteDetailController', FonctionnaliteDetailController);

    FonctionnaliteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fonctionnalite'];

    function FonctionnaliteDetailController($scope, $rootScope, $stateParams, previousState, entity, Fonctionnalite) {
        var vm = this;

        vm.fonctionnalite = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:fonctionnaliteUpdate', function(event, result) {
            vm.fonctionnalite = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
