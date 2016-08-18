(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BulletinDetailController', BulletinDetailController);

    BulletinDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bulletin', 'Eleve', 'Sequence'];

    function BulletinDetailController($scope, $rootScope, $stateParams, previousState, entity, Bulletin, Eleve, Sequence) {
        var vm = this;

        vm.bulletin = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('skulmanApp:bulletinUpdate', function(event, result) {
            vm.bulletin = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
