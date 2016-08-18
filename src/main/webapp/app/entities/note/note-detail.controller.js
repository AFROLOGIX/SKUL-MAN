(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('NoteDetailController', NoteDetailController);

    NoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Note', 'Sequence', 'Cours'];

    function NoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Note, Sequence, Cours) {
        var vm = this;

        vm.note = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:noteUpdate', function(event, result) {
            vm.note = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
