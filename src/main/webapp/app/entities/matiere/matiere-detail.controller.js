(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereDetailController', MatiereDetailController);

    MatiereDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Matiere'];

    function MatiereDetailController($scope, $rootScope, $stateParams, previousState, entity, Matiere) {
        var vm = this;

        vm.matiere = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:matiereUpdate', function(event, result) {
            vm.matiere = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
