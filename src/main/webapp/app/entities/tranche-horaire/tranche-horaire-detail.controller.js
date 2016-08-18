(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheHoraireDetailController', TrancheHoraireDetailController);

    TrancheHoraireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TrancheHoraire', 'TypeTrancheHoraire'];

    function TrancheHoraireDetailController($scope, $rootScope, $stateParams, previousState, entity, TrancheHoraire, TypeTrancheHoraire) {
        var vm = this;

        vm.trancheHoraire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:trancheHoraireUpdate', function(event, result) {
            vm.trancheHoraire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
