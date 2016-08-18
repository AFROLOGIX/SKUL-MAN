(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseDetailController', ClasseDetailController);

    ClasseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Classe', 'Serie', 'Niveau'];

    function ClasseDetailController($scope, $rootScope, $stateParams, previousState, entity, Classe, Serie, Niveau) {
        var vm = this;

        vm.classe = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:classeUpdate', function(event, result) {
            vm.classe = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
