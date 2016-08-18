(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('TrancheHoraireDialogController', TrancheHoraireDialogController);

    TrancheHoraireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'TrancheHoraire', 'TypeTrancheHoraire'];

    function TrancheHoraireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, TrancheHoraire, TypeTrancheHoraire) {
        var vm = this;

        vm.trancheHoraire = entity;
        vm.clear = clear;
        vm.save = save;
        vm.typetranchehoraires = TypeTrancheHoraire.query({filter: 'tranchehoraire-is-null'});
        $q.all([vm.trancheHoraire.$promise, vm.typetranchehoraires.$promise]).then(function() {
            if (!vm.trancheHoraire.typeTrancheHoraire || !vm.trancheHoraire.typeTrancheHoraire.id) {
                return $q.reject();
            }
            return TypeTrancheHoraire.get({id : vm.trancheHoraire.typeTrancheHoraire.id}).$promise;
        }).then(function(typeTrancheHoraire) {
            vm.typetranchehoraires.push(typeTrancheHoraire);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trancheHoraire.id !== null) {
                TrancheHoraire.update(vm.trancheHoraire, onSaveSuccess, onSaveError);
            } else {
                TrancheHoraire.save(vm.trancheHoraire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:trancheHoraireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
