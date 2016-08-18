(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereClasseDetailController', MatiereClasseDetailController);

    MatiereClasseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MatiereClasse', 'Classe', 'Matiere'];

    function MatiereClasseDetailController($scope, $rootScope, $stateParams, previousState, entity, MatiereClasse, Classe, Matiere) {
        var vm = this;

        vm.matiereClasse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:matiereClasseUpdate', function(event, result) {
            vm.matiereClasse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
