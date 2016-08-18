(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParentDetailController', ParentDetailController);

    ParentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Parent', 'Eleve'];

    function ParentDetailController($scope, $rootScope, $stateParams, previousState, entity, Parent, Eleve) {
        var vm = this;

        vm.parent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:parentUpdate', function(event, result) {
            vm.parent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
