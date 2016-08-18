(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsencePersonnelDetailController', AbsencePersonnelDetailController);

    AbsencePersonnelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AbsencePersonnel', 'Jour', 'Personnel'];

    function AbsencePersonnelDetailController($scope, $rootScope, $stateParams, previousState, entity, AbsencePersonnel, Jour, Personnel) {
        var vm = this;

        vm.absencePersonnel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:absencePersonnelUpdate', function(event, result) {
            vm.absencePersonnel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
