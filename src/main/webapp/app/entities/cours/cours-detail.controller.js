(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CoursDetailController', CoursDetailController);

    CoursDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cours', 'Matiere', 'Classe', 'Enseignant', 'Epreuve', 'Note'];

    function CoursDetailController($scope, $rootScope, $stateParams, previousState, entity, Cours, Matiere, Classe, Enseignant, Epreuve, Note) {
        var vm = this;

        vm.cours = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:coursUpdate', function(event, result) {
            vm.cours = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
