(function() {
    'use strict';

    angular
        .module('skulmanApp')
        .controller('EpreuveDialogController', EpreuveDialogController);

    EpreuveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Epreuve', 'TypeEpreuve', 'Cours'];

    function EpreuveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Epreuve, TypeEpreuve, Cours) {
        var vm = this;

        vm.epreuve = entity;
        vm.clear = clear;
        vm.save = save;
        vm.typeepreuves = TypeEpreuve.query({filter: 'epreuve-is-null'});
        $q.all([vm.epreuve.$promise, vm.typeepreuves.$promise]).then(function() {
            if (!vm.epreuve.typeEpreuve || !vm.epreuve.typeEpreuve.id) {
                return $q.reject();
            }
            return TypeEpreuve.get({id : vm.epreuve.typeEpreuve.id}).$promise;
        }).then(function(typeEpreuve) {
            vm.typeepreuves.push(typeEpreuve);
        });
        vm.cours = Cours.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.epreuve.id !== null) {
                Epreuve.update(vm.epreuve, onSaveSuccess, onSaveError);
            } else {
                Epreuve.save(vm.epreuve, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('skulmanApp:epreuveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
