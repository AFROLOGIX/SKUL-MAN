(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantTitulaireDialogController', EnseignantTitulaireDialogController);

    EnseignantTitulaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'EnseignantTitulaire', 'Classe'];

    function EnseignantTitulaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, EnseignantTitulaire, Classe) {
        var vm = this;

        vm.enseignantTitulaire = entity;
        vm.clear = clear;
        vm.save = save;
        vm.classes = Classe.query({filter: 'enseignanttitulaire-is-null'});
        $q.all([vm.enseignantTitulaire.$promise, vm.classes.$promise]).then(function() {
            if (!vm.enseignantTitulaire.classe || !vm.enseignantTitulaire.classe.id) {
                return $q.reject();
            }
            return Classe.get({id : vm.enseignantTitulaire.classe.id}).$promise;
        }).then(function(classe) {
            vm.classes.push(classe);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.enseignantTitulaire.id !== null) {
                EnseignantTitulaire.update(vm.enseignantTitulaire, onSaveSuccess, onSaveError);
            } else {
                EnseignantTitulaire.save(vm.enseignantTitulaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:enseignantTitulaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
