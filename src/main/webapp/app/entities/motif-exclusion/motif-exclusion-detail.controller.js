(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MotifExclusionDetailController', MotifExclusionDetailController);

    MotifExclusionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MotifExclusion'];

    function MotifExclusionDetailController($scope, $rootScope, $stateParams, previousState, entity, MotifExclusion) {
        var vm = this;

        vm.motifExclusion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:motifExclusionUpdate', function(event, result) {
            vm.motifExclusion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
