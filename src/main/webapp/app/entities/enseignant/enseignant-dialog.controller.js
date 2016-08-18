(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EnseignantDialogController', EnseignantDialogController);

    EnseignantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Enseignant', 'Personnel', 'AbsenceEnseignant', 'Vacation', 'Fichier', 'ProjetPedagogique', 'Deliberation'];

    function EnseignantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Enseignant, Personnel, AbsenceEnseignant, Vacation, Fichier, ProjetPedagogique, Deliberation) {
        var vm = this;

        vm.enseignant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.personnels = Personnel.query({filter: 'enseignant-is-null'});
        $q.all([vm.enseignant.$promise, vm.personnels.$promise]).then(function() {
            if (!vm.enseignant.personnel || !vm.enseignant.personnel.id) {
                return $q.reject();
            }
            return Personnel.get({id : vm.enseignant.personnel.id}).$promise;
        }).then(function(personnel) {
            vm.personnels.push(personnel);
        });
        vm.absenceenseignants = AbsenceEnseignant.query();
        vm.vacations = Vacation.query();
        vm.fichiers = Fichier.query();
        vm.projetpedagogiques = ProjetPedagogique.query();
        vm.deliberations = Deliberation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.enseignant.id !== null) {
                Enseignant.update(vm.enseignant, onSaveSuccess, onSaveError);
            } else {
                Enseignant.save(vm.enseignant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:enseignantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
