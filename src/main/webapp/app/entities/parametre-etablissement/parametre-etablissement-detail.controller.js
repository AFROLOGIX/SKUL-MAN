(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ParametreEtablissementDetailController', ParametreEtablissementDetailController);

    ParametreEtablissementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ParametreEtablissement'];

    function ParametreEtablissementDetailController($scope, $rootScope, $stateParams, previousState, entity, ParametreEtablissement) {
        var vm = this;

        vm.parametreEtablissement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:parametreEtablissementUpdate', function(event, result) {
            vm.parametreEtablissement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
