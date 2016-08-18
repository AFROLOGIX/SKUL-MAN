(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeEpreuveDetailController', TypeEpreuveDetailController);

    TypeEpreuveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeEpreuve'];

    function TypeEpreuveDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeEpreuve) {
        var vm = this;

        vm.typeEpreuve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typeEpreuveUpdate', function(event, result) {
            vm.typeEpreuve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
