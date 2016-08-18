(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('VacationDetailController', VacationDetailController);

    VacationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vacation', 'Enseignant'];

    function VacationDetailController($scope, $rootScope, $stateParams, previousState, entity, Vacation, Enseignant) {
        var vm = this;

        vm.vacation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:vacationUpdate', function(event, result) {
            vm.vacation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
