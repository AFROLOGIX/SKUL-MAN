(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ClasseEleveDetailController', ClasseEleveDetailController);

    ClasseEleveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClasseEleve', 'Classe', 'Eleve'];

    function ClasseEleveDetailController($scope, $rootScope, $stateParams, previousState, entity, ClasseEleve, Classe, Eleve) {
        var vm = this;

        vm.classeEleve = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:classeEleveUpdate', function(event, result) {
            vm.classeEleve = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
