(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('PeriodeSaisieNoteDetailController', PeriodeSaisieNoteDetailController);

    PeriodeSaisieNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PeriodeSaisieNote'];

    function PeriodeSaisieNoteDetailController($scope, $rootScope, $stateParams, previousState, entity, PeriodeSaisieNote) {
        var vm = this;

        vm.periodeSaisieNote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:periodeSaisieNoteUpdate', function(event, result) {
            vm.periodeSaisieNote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
