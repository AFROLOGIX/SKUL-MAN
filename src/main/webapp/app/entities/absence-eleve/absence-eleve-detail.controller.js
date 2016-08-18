(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AbsenceEleveDetailController', AbsenceEleveDetailController);

    AbsenceEleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AbsenceEleve', 'Jour', 'Eleve'];

    function AbsenceEleveDetailController($scope, $rootScope, $stateParams, previousState, entity, AbsenceEleve, Jour, Eleve) {
        var vm = this;

        vm.absenceEleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:absenceEleveUpdate', function(event, result) {
            vm.absenceEleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
