(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EleveDialogController', EleveDialogController);

    EleveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Eleve', 'ChambreEleve', 'Compte', 'Religion', 'AbsenceEleve', 'Bourse', 'Moratoire', 'Fichier'];

    function EleveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Eleve, ChambreEleve, Compte, Religion, AbsenceEleve, Bourse, Moratoire, Fichier) {
        var vm = this;

        vm.eleve = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.chambreeleves = ChambreEleve.query({filter: 'eleve-is-null'});
        $q.all([vm.eleve.$promise, vm.chambreeleves.$promise]).then(function() {
            if (!vm.eleve.chambreEleve || !vm.eleve.chambreEleve.id) {
                return $q.reject();
            }
            return ChambreEleve.get({id : vm.eleve.chambreEleve.id}).$promise;
        }).then(function(chambreEleve) {
            vm.chambreeleves.push(chambreEleve);
        });
        vm.comptes = Compte.query({filter: 'eleve-is-null'});
        $q.all([vm.eleve.$promise, vm.comptes.$promise]).then(function() {
            if (!vm.eleve.compte || !vm.eleve.compte.id) {
                return $q.reject();
            }
            return Compte.get({id : vm.eleve.compte.id}).$promise;
        }).then(function(compte) {
            vm.comptes.push(compte);
        });
        vm.religions = Religion.query({filter: 'eleve-is-null'});
        $q.all([vm.eleve.$promise, vm.religions.$promise]).then(function() {
            if (!vm.eleve.religion || !vm.eleve.religion.id) {
                return $q.reject();
            }
            return Religion.get({id : vm.eleve.religion.id}).$promise;
        }).then(function(religion) {
            vm.religions.push(religion);
        });
        vm.absenceeleves = AbsenceEleve.query();
        vm.bourses = Bourse.query();
        vm.moratoires = Moratoire.query();
        vm.fichiers = Fichier.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.eleve.id !== null) {
                Eleve.update(vm.eleve, onSaveSuccess, onSaveError);
            } else {
                Eleve.save(vm.eleve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:eleveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateNaiss = false;
        vm.datePickerOpenStatus.createAt = false;
        vm.datePickerOpenStatus.updateAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
