(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SequenceDetailController', SequenceDetailController);

    SequenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sequence', 'Trimestre'];

    function SequenceDetailController($scope, $rootScope, $stateParams, previousState, entity, Sequence, Trimestre) {
        var vm = this;

        vm.sequence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:sequenceUpdate', function(event, result) {
            vm.sequence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
