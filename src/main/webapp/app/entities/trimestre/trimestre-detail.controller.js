(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrimestreDetailController', TrimestreDetailController);

    TrimestreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Trimestre', 'Sequence'];

    function TrimestreDetailController($scope, $rootScope, $stateParams, previousState, entity, Trimestre, Sequence) {
        var vm = this;

        vm.trimestre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:trimestreUpdate', function(event, result) {
            vm.trimestre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
