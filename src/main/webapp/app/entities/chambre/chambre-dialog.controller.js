(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreDialogController', ChambreDialogController);

    ChambreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Chambre', 'Batiment', 'TypeChambre'];

    function ChambreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Chambre, Batiment, TypeChambre) {
        var vm = this;

        vm.chambre = entity;
        vm.clear = clear;
        vm.save = save;
        vm.batiments = Batiment.query({filter: 'chambre-is-null'});
        $q.all([vm.chambre.$promise, vm.batiments.$promise]).then(function() {
            if (!vm.chambre.batiment || !vm.chambre.batiment.id) {
                return $q.reject();
            }
            return Batiment.get({id : vm.chambre.batiment.id}).$promise;
        }).then(function(batiment) {
            vm.batiments.push(batiment);
        });
        vm.typechambres = TypeChambre.query({filter: 'chambre-is-null'});
        $q.all([vm.chambre.$promise, vm.typechambres.$promise]).then(function() {
            if (!vm.chambre.typeChambre || !vm.chambre.typeChambre.id) {
                return $q.reject();
            }
            return TypeChambre.get({id : vm.chambre.typeChambre.id}).$promise;
        }).then(function(typeChambre) {
            vm.typechambres.push(typeChambre);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chambre.id !== null) {
                Chambre.update(vm.chambre, onSaveSuccess, onSaveError);
            } else {
                Chambre.save(vm.chambre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:chambreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
