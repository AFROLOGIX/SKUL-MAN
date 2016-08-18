(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SerieDetailController', SerieDetailController);

    SerieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Serie'];

    function SerieDetailController($scope, $rootScope, $stateParams, previousState, entity, Serie) {
        var vm = this;

        vm.serie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:serieUpdate', function(event, result) {
            vm.serie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
