(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeMoratoireDetailController', TypeMoratoireDetailController);

    TypeMoratoireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeMoratoire'];

    function TypeMoratoireDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeMoratoire) {
        var vm = this;

        vm.typeMoratoire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typeMoratoireUpdate', function(event, result) {
            vm.typeMoratoire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
