(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TypeTrancheHoraireDetailController', TypeTrancheHoraireDetailController);

    TypeTrancheHoraireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeTrancheHoraire'];

    function TypeTrancheHoraireDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeTrancheHoraire) {
        var vm = this;

        vm.typeTrancheHoraire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:typeTrancheHoraireUpdate', function(event, result) {
            vm.typeTrancheHoraire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
