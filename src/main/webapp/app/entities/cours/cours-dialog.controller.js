(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('CoursDialogController', CoursDialogController);

    CoursDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Cours', 'Matiere', 'Classe', 'Enseignant', 'Epreuve', 'Note'];

    function CoursDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Cours, Matiere, Classe, Enseignant, Epreuve, Note) {
        var vm = this;

        vm.cours = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.matieres = Matiere.query({filter: 'cours-is-null'});
        $q.all([vm.cours.$promise, vm.matieres.$promise]).then(function() {
            if (!vm.cours.matiere || !vm.cours.matiere.id) {
                return $q.reject();
            }
            return Matiere.get({id : vm.cours.matiere.id}).$promise;
        }).then(function(matiere) {
            vm.matieres.push(matiere);
        });
        vm.classes = Classe.query({filter: 'cours-is-null'});
        $q.all([vm.cours.$promise, vm.classes.$promise]).then(function() {
            if (!vm.cours.classe || !vm.cours.classe.id) {
                return $q.reject();
            }
            return Classe.get({id : vm.cours.classe.id}).$promise;
        }).then(function(classe) {
            vm.classes.push(classe);
        });
        vm.enseignants = Enseignant.query({filter: 'cours-is-null'});
        $q.all([vm.cours.$promise, vm.enseignants.$promise]).then(function() {
            if (!vm.cours.enseignant || !vm.cours.enseignant.id) {
                return $q.reject();
            }
            return Enseignant.get({id : vm.cours.enseignant.id}).$promise;
        }).then(function(enseignant) {
            vm.enseignants.push(enseignant);
        });
        vm.epreuves = Epreuve.query();
        vm.notes = Note.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cours.id !== null) {
                Cours.update(vm.cours, onSaveSuccess, onSaveError);
            } else {
                Cours.save(vm.cours, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:coursUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createAt = false;
        vm.datePickerOpenStatus.updateAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
