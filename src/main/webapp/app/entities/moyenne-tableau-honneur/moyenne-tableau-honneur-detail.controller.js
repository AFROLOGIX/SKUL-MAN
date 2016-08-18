(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoyenneTableauHonneurDetailController', MoyenneTableauHonneurDetailController);

    MoyenneTableauHonneurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MoyenneTableauHonneur'];

    function MoyenneTableauHonneurDetailController($scope, $rootScope, $stateParams, previousState, entity, MoyenneTableauHonneur) {
        var vm = this;

        vm.moyenneTableauHonneur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:moyenneTableauHonneurUpdate', function(event, result) {
            vm.moyenneTableauHonneur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
