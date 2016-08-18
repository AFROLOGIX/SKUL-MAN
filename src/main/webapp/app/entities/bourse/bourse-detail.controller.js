(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BourseDetailController', BourseDetailController);

    BourseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bourse', 'Eleve'];

    function BourseDetailController($scope, $rootScope, $stateParams, previousState, entity, Bourse, Eleve) {
        var vm = this;

        vm.bourse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:bourseUpdate', function(event, result) {
            vm.bourse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
