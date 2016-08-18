(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('AgentAdministratifDialogController', AgentAdministratifDialogController);

    AgentAdministratifDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AgentAdministratif', 'Salaire', 'Fichier', 'Deliberation'];

    function AgentAdministratifDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AgentAdministratif, Salaire, Fichier, Deliberation) {
        var vm = this;

        vm.agentAdministratif = entity;
        vm.clear = clear;
        vm.save = save;
        vm.salaires = Salaire.query({filter: 'agentadministratif-is-null'});
        $q.all([vm.agentAdministratif.$promise, vm.salaires.$promise]).then(function() {
            if (!vm.agentAdministratif.salaire || !vm.agentAdministratif.salaire.id) {
                return $q.reject();
            }
            return Salaire.get({id : vm.agentAdministratif.salaire.id}).$promise;
        }).then(function(salaire) {
            vm.salaires.push(salaire);
        });
        vm.fichiers = Fichier.query();
        vm.deliberations = Deliberation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agentAdministratif.id !== null) {
                AgentAdministratif.update(vm.agentAdministratif, onSaveSuccess, onSaveError);
            } else {
                AgentAdministratif.save(vm.agentAdministratif, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:agentAdministratifUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
