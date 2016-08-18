(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ProjetPedagogiqueDetailController', ProjetPedagogiqueDetailController);

    ProjetPedagogiqueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjetPedagogique', 'Classe', 'Enseignant'];

    function ProjetPedagogiqueDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjetPedagogique, Classe, Enseignant) {
        var vm = this;

        vm.projetPedagogique = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:projetPedagogiqueUpdate', function(event, result) {
            vm.projetPedagogique = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
