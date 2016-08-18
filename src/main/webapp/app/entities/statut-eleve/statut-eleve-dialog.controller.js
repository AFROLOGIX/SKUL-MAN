(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('StatutEleveDialogController', StatutEleveDialogController);

    StatutEleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'StatutEleve', 'Eleve', 'Statut'];

    function StatutEleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, StatutEleve, Eleve, Statut) {
        var vm = this;

        vm.statutEleve = entity;
        vm.clear = clear;
        vm.save = save;
        vm.eleves = Eleve.query({filter: 'statuteleve-is-null'});
        $q.all([vm.statutEleve.$promise, vm.eleves.$promise]).then(function() {
            if (!vm.statutEleve.eleve || !vm.statutEleve.eleve.id) {
                return $q.reject();
            }
            return Eleve.get({id : vm.statutEleve.eleve.id}).$promise;
        }).then(function(eleve) {
            vm.eleves.push(eleve);
        });
        vm.statuts = Statut.query({filter: 'statuteleve-is-null'});
        $q.all([vm.statutEleve.$promise, vm.statuts.$promise]).then(function() {
            if (!vm.statutEleve.statut || !vm.statutEleve.statut.id) {
                return $q.reject();
            }
            return Statut.get({id : vm.statutEleve.statut.id}).$promise;
        }).then(function(statut) {
            vm.statuts.push(statut);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.statutEleve.id !== null) {
                StatutEleve.update(vm.statutEleve, onSaveSuccess, onSaveError);
            } else {
                StatutEleve.save(vm.statutEleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:statutEleveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
