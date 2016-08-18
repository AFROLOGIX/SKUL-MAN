(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('FormatMatriculeDetailController', FormatMatriculeDetailController);

    FormatMatriculeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FormatMatricule'];

    function FormatMatriculeDetailController($scope, $rootScope, $stateParams, previousState, entity, FormatMatricule) {
        var vm = this;

        vm.formatMatricule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:formatMatriculeUpdate', function(event, result) {
            vm.formatMatricule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
