(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ReligionDetailController', ReligionDetailController);

    ReligionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Religion'];

    function ReligionDetailController($scope, $rootScope, $stateParams, previousState, entity, Religion) {
        var vm = this;

        vm.religion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:religionUpdate', function(event, result) {
            vm.religion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
