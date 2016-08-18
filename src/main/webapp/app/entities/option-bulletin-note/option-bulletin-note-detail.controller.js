(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('OptionBulletinNoteDetailController', OptionBulletinNoteDetailController);

    OptionBulletinNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OptionBulletinNote'];

    function OptionBulletinNoteDetailController($scope, $rootScope, $stateParams, previousState, entity, OptionBulletinNote) {
        var vm = this;

        vm.optionBulletinNote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:optionBulletinNoteUpdate', function(event, result) {
            vm.optionBulletinNote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
