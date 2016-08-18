(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PauseDetailController', PauseDetailController);

    PauseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pause'];

    function PauseDetailController($scope, $rootScope, $stateParams, previousState, entity, Pause) {
        var vm = this;

        vm.pause = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:pauseUpdate', function(event, result) {
            vm.pause = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
