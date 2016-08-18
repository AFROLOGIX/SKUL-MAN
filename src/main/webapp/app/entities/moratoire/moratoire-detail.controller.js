(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MoratoireDetailController', MoratoireDetailController);

    MoratoireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Moratoire', 'TypeMoratoire', 'Eleve'];

    function MoratoireDetailController($scope, $rootScope, $stateParams, previousState, entity, Moratoire, TypeMoratoire, Eleve) {
        var vm = this;

        vm.moratoire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:moratoireUpdate', function(event, result) {
            vm.moratoire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
