(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('BulletinDialogController', BulletinDialogController);

    BulletinDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Bulletin', 'Eleve', 'Sequence'];

    function BulletinDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Bulletin, Eleve, Sequence) {
        var vm = this;

        vm.bulletin = entity;
        vm.clear = clear;
        vm.save = save;
        vm.eleves = Eleve.query({filter: 'bulletin-is-null'});
        $q.all([vm.bulletin.$promise, vm.eleves.$promise]).then(function() {
            if (!vm.bulletin.eleve || !vm.bulletin.eleve.id) {
                return $q.reject();
            }
            return Eleve.get({id : vm.bulletin.eleve.id}).$promise;
        }).then(function(eleve) {
            vm.eleves.push(eleve);
        });
        vm.sequences = Sequence.query({filter: 'bulletin-is-null'});
        $q.all([vm.bulletin.$promise, vm.sequences.$promise]).then(function() {
            if (!vm.bulletin.sequence || !vm.bulletin.sequence.id) {
                return $q.reject();
            }
            return Sequence.get({id : vm.bulletin.sequence.id}).$promise;
        }).then(function(sequence) {
            vm.sequences.push(sequence);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bulletin.id !== null) {
                Bulletin.update(vm.bulletin, onSaveSuccess, onSaveError);
            } else {
                Bulletin.save(vm.bulletin, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:bulletinUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
