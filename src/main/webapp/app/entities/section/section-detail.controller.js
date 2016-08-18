(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SectionDetailController', SectionDetailController);

    SectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Section', 'Cycle'];

    function SectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Section, Cycle) {
        var vm = this;

        vm.section = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:sectionUpdate', function(event, result) {
            vm.section = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
