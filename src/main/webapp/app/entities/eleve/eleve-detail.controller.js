(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EleveDetailController', EleveDetailController);

    EleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Eleve', 'ChambreEleve', 'Compte', 'Religion', 'AbsenceEleve', 'Bourse', 'Moratoire', 'Fichier'];

    function EleveDetailController($scope, $rootScope, $stateParams, previousState, entity, Eleve, ChambreEleve, Compte, Religion, AbsenceEleve, Bourse, Moratoire, Fichier) {
        var vm = this;

        vm.eleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:eleveUpdate', function(event, result) {
            vm.eleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
