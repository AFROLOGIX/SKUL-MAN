(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EtablissementDetailController', EtablissementDetailController);

    EtablissementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Etablissement'];

    function EtablissementDetailController($scope, $rootScope, $stateParams, previousState, entity, Etablissement) {
        var vm = this;

        vm.etablissement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:etablissementUpdate', function(event, result) {
            vm.etablissement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
