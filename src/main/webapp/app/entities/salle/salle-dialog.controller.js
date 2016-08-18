(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('SalleDialogController', SalleDialogController);

    SalleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Salle'];

    function SalleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Salle) {
        var vm = this;

        vm.salle = entity;
        vm.clear = clear;
        vm.save = save;
        vm.batiments = Salle.query({filter: 'salle-is-null'});
        $q.all([vm.salle.$promise, vm.batiments.$promise]).then(function() {
            if (!vm.salle.batiment || !vm.salle.batiment.id) {
                return $q.reject();
            }
            return Salle.get({id : vm.salle.batiment.id}).$promise;
        }).then(function(batiment) {
            vm.batiments.push(batiment);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.salle.id !== null) {
                Salle.update(vm.salle, onSaveSuccess, onSaveError);
            } else {
                Salle.save(vm.salle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:salleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
