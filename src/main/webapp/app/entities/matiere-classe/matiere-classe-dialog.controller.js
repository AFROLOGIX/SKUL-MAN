(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('MatiereClasseDialogController', MatiereClasseDialogController);

    MatiereClasseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MatiereClasse', 'Classe', 'Matiere'];

    function MatiereClasseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MatiereClasse, Classe, Matiere) {
        var vm = this;

        vm.matiereClasse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.classes = Classe.query({filter: 'matiereclasse-is-null'});
        $q.all([vm.matiereClasse.$promise, vm.classes.$promise]).then(function() {
            if (!vm.matiereClasse.classe || !vm.matiereClasse.classe.id) {
                return $q.reject();
            }
            return Classe.get({id : vm.matiereClasse.classe.id}).$promise;
        }).then(function(classe) {
            vm.classes.push(classe);
        });
        vm.matieres = Matiere.query({filter: 'matiereclasse-is-null'});
        $q.all([vm.matiereClasse.$promise, vm.matieres.$promise]).then(function() {
            if (!vm.matiereClasse.matiere || !vm.matiereClasse.matiere.id) {
                return $q.reject();
            }
            return Matiere.get({id : vm.matiereClasse.matiere.id}).$promise;
        }).then(function(matiere) {
            vm.matieres.push(matiere);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.matiereClasse.id !== null) {
                MatiereClasse.update(vm.matiereClasse, onSaveSuccess, onSaveError);
            } else {
                MatiereClasse.save(vm.matiereClasse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:matiereClasseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
