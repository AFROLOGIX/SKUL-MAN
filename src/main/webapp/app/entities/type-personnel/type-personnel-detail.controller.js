(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypePersonnelDetailController', TypePersonnelDetailController);

    TypePersonnelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypePersonnel'];

    function TypePersonnelDetailController($scope, $rootScope, $stateParams, previousState, entity, TypePersonnel) {
        var vm = this;

        vm.typePersonnel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typePersonnelUpdate', function(event, result) {
            vm.typePersonnel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
