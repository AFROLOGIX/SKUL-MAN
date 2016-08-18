(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('ChambreEleveDialogController', ChambreEleveDialogController);

    ChambreEleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ChambreEleve', 'Chambre'];

    function ChambreEleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ChambreEleve, Chambre) {
        var vm = this;

        vm.chambreEleve = entity;
        vm.clear = clear;
        vm.save = save;
        vm.chambres = Chambre.query({filter: 'chambreeleve-is-null'});
        $q.all([vm.chambreEleve.$promise, vm.chambres.$promise]).then(function() {
            if (!vm.chambreEleve.chambre || !vm.chambreEleve.chambre.id) {
                return $q.reject();
            }
            return Chambre.get({id : vm.chambreEleve.chambre.id}).$promise;
        }).then(function(chambre) {
            vm.chambres.push(chambre);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chambreEleve.id !== null) {
                ChambreEleve.update(vm.chambreEleve, onSaveSuccess, onSaveError);
            } else {
                ChambreEleve.save(vm.chambreEleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:chambreEleveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
